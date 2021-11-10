package cn.blueisacat.browser;

import org.openqa.selenium.WebDriver;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public abstract class Browser {

    protected abstract WebDriver initWebDriver();

    public Browser() {
        webDriver = initWebDriver();
        status = BrowserStatus.NOT_USE;
        startTime = new Date().getTime();
    }

    private WebDriver webDriver;

    private BrowserStatus status;

    private Long startTime;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public BrowserStatus getStatus() {
        return status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public boolean test() {
        try {
            webDriver.getCurrentUrl();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void release() {
        this.status = BrowserStatus.NOT_USE;
    }

    public Browser get() {
        this.status = BrowserStatus.IN_USE;
        return this;
    }

    public Browser getWithTest() {
        if (!test()) {
            destroy();
            this.webDriver = initWebDriver();
        }
        this.status = BrowserStatus.IN_USE;
        return this;
    }

    public void destroy() {
        this.status = BrowserStatus.DESTROY;
        new Thread(() -> {
            if (null != this.webDriver) {
                try {
                    this.webDriver.quit();
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void setTimeout(long timeoutInSecond) {
        if (null != this.webDriver) {
            this.webDriver.manage().timeouts().pageLoadTimeout(timeoutInSecond, TimeUnit.SECONDS);
            this.webDriver.manage().timeouts().implicitlyWait(timeoutInSecond, TimeUnit.SECONDS);
            this.webDriver.manage().timeouts().setScriptTimeout(timeoutInSecond, TimeUnit.SECONDS);
        }
    }

    public enum BrowserStatus {
        NOT_USE(0), // 未使用
        IN_USE(1), // 使用中
        DESTROY(2); // 已销毁

        private int value;

        private BrowserStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
