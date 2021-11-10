package cn.blueisacat.browser.impl;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.utils.ConfigUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

/**
 * @Title: ChromeBrowser
 * @author: gc
 * @Description: ChromeBrowser
 */
public class ChromeBrowser extends Browser {

    private static final String DRIVER_PATH = ConfigUtils.getInstance().getStringVal("browser.driver");
    private static final String BINARY_PATH = ConfigUtils.getInstance().getStringVal("browser.binary");

    private static final boolean headless = true;
    private static final boolean loadImgResources = false;

    private static final String id = "chrome";

    @Override
    protected WebDriver initWebDriver() {
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
