# 1. 工具介绍

selenium是一款测试工具，使用webDriver操作浏览器进行真实的web端测试。

在实际应用中，通常会对浏览器进行并发的操作使用，那么对浏览器的管理就变得很麻烦了，所以本工具产生了，本工具对浏览器进行统一的管理，让开发者聚焦在使用webDriver上。

# 2. 如何使用

## 2.1 pom.xml

~~~xml
<!-- https://mvnrepository.com/artifact/cn.blueisacat/browser -->
<dependency>
    <groupId>cn.blueisacat</groupId>
    <artifactId>browser</artifactId>
    <version>3.1</version>
</dependency>

~~~

核心使用selenium-java的4.0.0版本，根据实际使用可以更换对应的selenium版本。

## 2.2 配置文件

配置文件browser.yml如下：

~~~yaml
browser:
  id: chrome #firefox chrome
  driver: pathToDriver #浏览器驱动路径
  binary: pathToBinary #浏览器执行文件路径
  headless: true #浏览器是否开启无头模式
  loadImgResources: true #浏览器是否加载图片
  pool:
    poolMaxSize: 2 #浏览器池最大值
    poolInitSize: 1 #浏览器池初始化值
    testOnBorrow: true  #获取浏览器时是否测试可用性
    recycleInSecond: 60 #回收浏览器时间间隔
~~~

以上为browser对应的配置项，详细介绍如下：

* browser.id：浏览器类型，目前只支持选择其中一种浏览器进行使用，可选值[firefox，chrome]。
* browser.driver：浏览器驱动路径，驱动版本需要与浏览器版本对应，下载地址参考“3.资源下载”
* browser.binary：浏览器执执行文件路径，浏览器版本需要与驱动版本对应。
* browser.headless：浏览器是否开启无头模式，开启无头模式可以节省资源。
* browser.loadImgResources：浏览器是否加载图片，关闭后可节省带宽。
* browser.pool.poolMaxSize：浏览器池最大值，由于浏览器消耗CPU资源大，建议最大值不超过CPU的逻辑核数。
* browser.pool.poolInitSize：浏览器池初始化值，建议初始化值为0，随用随开，节省资源。
* browser.pool.testOnBorrow：获取浏览器时是否测试可用性，建议开启，保证程序获取的浏览器可用性。
* browser.pool.recycleInSecond：回收浏览器时间间隔，每次间隔后检测浏览器是否使用，如果已经不再使用，进行回收（销毁）处理，建议60，单位为秒。

考虑到windows及linux下webDriver的通用性，暂时只兼容了firefox及chrome两种主流浏览器，至于phatomJS已经很长时间无人维护，所以也放弃兼容，如有自定义浏览器的需求，请参考“2.4自定义浏览器”。

配置文件加载顺序如下：

1. browser.config.location指定配置文件
2. 当前目录下的browser.yml
3. 当前目录下的config文件夹中的browser.yml
4. jar包内的browser.yml

## 2.3 使用教程

1. 创建执行器
2. 构造请求参数
3. 执行业务方法
4. 获取执行结果

使用方法1：使用内置FetchPageSourceFunction方法获取页面源代码

~~~java
public class Test {

    public static void main(String[] args) {
        // 创建执行器
        BrowserExecutor<FetchPageSourceFunction.Request, FetchPageSourceFunction.Response> executor = new BrowserExecutor<FetchPageSourceFunction.Request, FetchPageSourceFunction.Response>();
        // 构造请求参数
        FetchPageSourceFunction.Request request = new FetchPageSourceFunction.Request("https://www.baidu.com");
        // 执行业务方法
        BrowserExecutorResult<FetchPageSourceFunction.Response> result = executor.execute(new FetchPageSourceFunction(), request);
        // 获取执行结果
        System.out.println(result.get().getPageSource());
    }

}
~~~

使用方法2：使用自定义方法执行业务操作

~~~java
public class Test {

    public static void main(String[] args) {
        // 创建执行器
        BrowserExecutor<String, String> executor = new BrowserExecutor<String, String>();
        // 构造请求参数
        String url = "http://www.baidu.com";
        // 执行业务方法
        BrowserExecutorResult<String> result = executor.execute(new BrowserExecutorFunction<String, String>() {
            @Override
            protected BrowserExecutorResult<String> doBusiness(Browser browser, String url) throws Exception {
                browser.getWebDriver().get(url);
                Thread.sleep(1_000);
                String pageSource = browser.getWebDriver().getPageSource();
                return new BrowserExecutorResult<String>(pageSource);
            }
        }, url);
        // 获取执行结果
        System.out.println(result.get());
    }

}
~~~

其中selenium以及webDriver的使用请参考“4.参考资料”。

## 2.4 自定义浏览器

当前默认支持Chome及Firefox，如有自定义浏览器（修改浏览器配置项、增加其他浏览器支持等）的需求，拓展方法如下：

1. 继承Browser并实现initWebDriver方法
2. 设置全局唯一浏览器id

```java
public class CustomBrowser extends Browser {

    private static final String DRIVER_PATH = ConfigUtils.getInstance().getStringVal("browser.driver");
    private static final String BINARY_PATH = ConfigUtils.getInstance().getStringVal("browser.binary");

    // 全局唯一浏览器id
    private static final String id = "globalUniqueBrowserId";

    @Override
    protected WebDriver initWebDriver() {
        // 实例化WebDriver
        return webDriver;
    }
}
```

3. 在配置文件browser.yml中修改browser.id的值为自定义的全局唯一浏览器id

~~~yaml
browser:
  id: globalUniqueBrowserId #firefox chrome
  driver: pathToDriver #浏览器驱动路径
  binary: pathToBinary #浏览器执行文件路径
  pool:
    poolMaxSize: 2 #浏览器池最大值
    poolInitSize: 1 #浏览器池初始化值
    testOnBorrow: true  #获取浏览器时是否测试可用性
    recycleInSecond: 60 #回收浏览器时间间隔
~~~

# 3. 资源下载

> chromeDriver:
>* http://npm.taobao.org/mirrors/chromedriver/
>* http://chromedriver.storage.googleapis.com/index.html

> geckodriver:
>* http://npm.taobao.org/mirrors/geckodriver/
>* https://github.com/mozilla/geckodriver/releases

# 4. 参考资料

> selenium java api：
>* https://www.selenium.dev/selenium/docs/api/java/overview-summary.html
