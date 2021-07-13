package cn.blueisacat;

import org.openqa.selenium.WebDriver;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public class BrowserWebDriver {

    public static WebDriver createDefault() {
        return new BrowserWebDriverBuilder().build();
    }

    public static BrowserWebDriverBuilder custom() {
        return new BrowserWebDriverBuilder();
    }


}

