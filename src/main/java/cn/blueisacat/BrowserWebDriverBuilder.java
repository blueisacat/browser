package cn.blueisacat;

import cn.blueisacat.utils.ConfigUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.*;

import java.io.File;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public class BrowserWebDriverBuilder {

    private static final String TYPE = ConfigUtils.getInstance().getStringVal("browser.type");
    private static final String DRIVER_PATH = ConfigUtils.getInstance().getStringVal("browser.driver");
    private static final String BINARY_PATH = ConfigUtils.getInstance().getStringVal("browser.binary");

    private boolean headless = true;
    private boolean loadImgResources = false;

    public BrowserWebDriverBuilder setHeadless(boolean headless) {
        this.headless = headless;
        return this;
    }

    public BrowserWebDriverBuilder setLoadImgResources(boolean loadImgResources) {
        this.loadImgResources = loadImgResources;
        return this;
    }

    public WebDriver build() {
        switch (TYPE) {
            case "firefox":
                return createFirefoxWebDriver();
            case "chrome":
                return createChromeWebDriver();
            default:
                return createFirefoxWebDriver();
        }
    }

    public WebDriver createFirefoxWebDriver() {
        GeckoDriverService geckoDriverService = new GeckoDriverService.Builder().usingAnyFreePort().usingDriverExecutable(new File(DRIVER_PATH)).build();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(BINARY_PATH);
        firefoxOptions.setHeadless(headless);
        firefoxOptions.setLegacy(false);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.ERROR);
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (!loadImgResources) {
            firefoxOptions.addPreference("permissions.default.image", 2);
        }
        WebDriver webDriver = new FirefoxDriver(geckoDriverService, firefoxOptions);
        return webDriver;
    }

    public WebDriver createChromeWebDriver() {
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder().usingAnyFreePort().usingDriverExecutable(new File(DRIVER_PATH)).build();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(BINARY_PATH);
        chromeOptions.setHeadless(headless);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (!loadImgResources) {
            chromeOptions.addArguments("–disable-images");
        }
        WebDriver webDriver = new ChromeDriver(chromeDriverService, chromeOptions);
        return webDriver;
    }

}
