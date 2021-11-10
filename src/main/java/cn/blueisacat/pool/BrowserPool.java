package cn.blueisacat.pool;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.browser.impl.ChromeBrowser;
import cn.blueisacat.utils.ConfigUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public class BrowserPool {

    private static BrowserPool browserPool = new BrowserPool();

    public static BrowserPool getInstance() {
        return browserPool;
    }

    private BrowserPool() {
        initPool();
    }

    private void initPool() {
        // 读取浏览器类
        if (null != ConfigUtils.getInstance().getStringVal("browser.id")) {
            this.browserClass = getBrowserClass();
        }
        // 读取配置[池最大值]
        if (null != ConfigUtils.getInstance().getIntegerVal("browser.pool.poolMaxSize")) {
            this.poolMaxSize = ConfigUtils.getInstance().getIntegerVal("browser.pool.poolMaxSize");
        }
        // 读取配置[池初始值]
        if (null != ConfigUtils.getInstance().getIntegerVal("browser.pool.poolInitSize")) {
            this.poolInitSize = ConfigUtils.getInstance().getIntegerVal("browser.pool.poolInitSize");
        }
        // 读取配置[获取浏览器时是否进行验证]
        if (null != ConfigUtils.getInstance().getBooleanVal("browser.pool.testOnBorrow")) {
            this.testOnBorrow = ConfigUtils.getInstance().getBooleanVal("browser.pool.testOnBorrow");
        }
        // 读取配置[回收闲置浏览器周期]
        if (null != ConfigUtils.getInstance().getBooleanVal("browser.pool.recycleInSecond")) {
            this.recycleInSecond = ConfigUtils.getInstance().getIntegerVal("browser.pool.recycleInSecond");
        }
        // 校验poolMaxSize
        if (poolMaxSize <= 0) {
            poolMaxSize = Runtime.getRuntime().availableProcessors() + 1;
        }
        // 校验poolInitSize
        if (poolInitSize < 0) {
            poolInitSize = 0;
        }
        // 校验poolInitSize必须小于poolMaxSize
        if (poolInitSize > poolMaxSize) {
            poolInitSize = poolMaxSize;
        }
        // 校验recycleInSecond必须大于60
        if (recycleInSecond < 60) {
            recycleInSecond = 60;
        }
        // 初始化
        if (this.poolInitSize > 0) {
            lock.lock();
            try {
                for (int i = 0; i < poolInitSize; i++) {
                    Browser browser = null;
                    try {
                        browser = (Browser) browserClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    browserList.add(browser);
                }
            } finally {
                lock.unlock();
            }
        }
        // 回收闲置浏览器线程
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    List<Browser> tmp = Lists.newArrayList();
                    long thisTime = new Date().getTime();
                    for (Browser browser : browserList) {
                        // 回收闲置浏览器
                        if (thisTime - browser.getStartTime() > recycleInSecond * 1000) {
                            if (browser.getStatus() == Browser.BrowserStatus.NOT_USE) {
                                browser.destroy();
                            }
                        }
                        if (browser.getStatus() != Browser.BrowserStatus.DESTROY) {
                            tmp.add(browser);
                        }
                    }
                    this.browserList = tmp;
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep((recycleInSecond * 1000) / 10);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private Class browserClass = ChromeBrowser.class;

    private List<Browser> browserList = Lists.newArrayList();

    private int poolMaxSize = Runtime.getRuntime().availableProcessors() + 1;

    private int poolInitSize = 0;

    private boolean testOnBorrow = false;

    private int recycleInSecond = 3600;

    private ReentrantLock lock = new ReentrantLock(true);

    public Browser getBrowser() {
        lock.lock();
        try {
            checkBrowser();
            Browser browser = getIdleBrowser();
            if (null != browser) {
                if (testOnBorrow) {
                    return browser.getWithTest();
                } else {
                    return browser.get();
                }
            } else {
                if (browserList.size() < poolMaxSize) {
                    try {
                        browser = (Browser) browserClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    browserList.add(browser);
                    if (testOnBorrow) {
                        return browser.getWithTest();
                    } else {
                        return browser.get();
                    }
                } else {
                    while (null == browser) {
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e) {
                        }
                        browser = getIdleBrowser();
                    }
                    if (testOnBorrow) {
                        return browser.getWithTest();
                    } else {
                        return browser.get();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private Browser getIdleBrowser() {
        for (Browser browser : browserList) {
            if (browser.getStatus() == Browser.BrowserStatus.NOT_USE) {
                return browser;
            }
        }
        return null;
    }

    private void checkBrowser() {
        List<Browser> tmp = Lists.newArrayList();
        for (Browser browser : browserList) {
            if (browser.getStatus() != Browser.BrowserStatus.DESTROY) {
                tmp.add(browser);
            }
        }
        this.browserList = tmp;
    }

    public void clearPool() {
        lock.lock();
        try {
            for (Browser browser : browserList) {
                browser.destroy();
            }
            browserList.clear();
        } finally {
            lock.unlock();
        }
    }

    public Map<String, Object> getPoolInfo() {
        Map<String, Object> info = Maps.newConcurrentMap();
        info.put("poolMaxSize", poolMaxSize);
        info.put("poolInitSize", poolInitSize);
        info.put("testOnBorrow", testOnBorrow);
        info.put("currentPoolSize", browserList.size());
        return info;
    }

    public Class getBrowserClass() {
        try {
            ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
            Optional<? extends Class<?>> optional = classPath.getTopLevelClasses().stream().filter(classInfo -> {
                boolean isAssignableFromBrowser = false;
                try {
                    isAssignableFromBrowser = Browser.class.isAssignableFrom(classInfo.load()) && !Browser.class.getName().equals(classInfo.getName());
                } catch (Throwable e) {
                }
                return isAssignableFromBrowser;
            }).filter(classInfo -> {
                boolean isChoose = false;
                try {
                    Field idField = classInfo.load().getDeclaredField("id");
                    idField.setAccessible(true);
                    String id = (String) idField.get(classInfo.load());
                    isChoose = ConfigUtils.getInstance().getStringVal("browser.id").equals(id);
                } catch (Throwable e) {
                }
                return isChoose;
            }).map(classInfo -> classInfo.load()).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            } else {
                return ChromeBrowser.class;
            }
        } catch (Exception e) {
            return ChromeBrowser.class;
        }
    }

}
