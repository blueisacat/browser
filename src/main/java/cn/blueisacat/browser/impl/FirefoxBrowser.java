package cn.blueisacat.browser.impl;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.utils.ConfigUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.File;

/**
 * @Title: FirefoxBrowser
 * @author: gc
 * @Description: FirefoxBrowser
 */
public class FirefoxBrowser extends Browser {

    private static final String DRIVER_PATH = ConfigUtils.getInstance().getStringVal("browser.driver");
    private static final String BINARY_PATH = ConfigUtils.getInstance().getStringVal("browser.binary");

    private static final boolean headless = true;
    private static final boolean loadImgResources = false;

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
        if (!loadImgResources) {
            firefoxOptions.addPreference("permissions.default.image", 2);
        }
        WebDriver webDriver = new FirefoxDriver(geckoDriverService, firefoxOptions);
        return webDriver;
    }
}
