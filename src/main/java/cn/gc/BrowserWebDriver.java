package cn.gc;

import org.openqa.selenium.WebDriver;

/**
 * @author : gc
 * @date : 2021/6/25
 */
public class BrowserWebDriver {

    public static WebDriver createDefault() {
        return new BrowserWebDriverBuilder().build();
    }

    public static BrowserWebDriverBuilder custom() {
        return new BrowserWebDriverBuilder();
    }


}

