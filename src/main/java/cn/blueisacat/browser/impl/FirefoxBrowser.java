package cn.blueisacat.browser.impl;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.utils.ConfigUtils;
import com.google.common.collect.Maps;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.File;
import java.util.Map;

/**
 * @Title: FirefoxBrowser
 * @author: gc
 * @Description: FirefoxBrowser
 */
public class FirefoxBrowser extends Browser {

    private static final String DRIVER_PATH = ConfigUtils.getInstance().getStringVal("browser.driver");
    private static final String BINARY_PATH = ConfigUtils.getInstance().getStringVal("browser.binary");

    private static final boolean headless = ConfigUtils.getInstance().getBooleanVal("browser.headless");
    private static final boolean loadImgResources = ConfigUtils.getInstance().getBooleanVal("browser.loadImgResources");

    private static final String id = "firefox";

    @Override
    protected WebDriver initWebDriver() {
        GeckoDriverService geckoDriverService = new GeckoDriverService.Builder().usingAnyFreePort().usingDriverExecutable(new File(DRIVER_PATH)).build();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(BINARY_PATH);
        firefoxOptions.setHeadless(headless);
        firefoxOptions.setLegacy(false);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.ERROR);
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        firefoxOptions.addPreference("dom.webdriver.enabled", false);
        if (!loadImgResources) {
            firefoxOptions.addPreference("permissions.default.image", 2);
        }
        FirefoxDriver webDriver = new FirefoxDriver(geckoDriverService, firefoxOptions);
        return webDriver;
    }


    public void antiDetect() {
    }


}
