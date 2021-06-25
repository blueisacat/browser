package cn.gc;

import org.openqa.selenium.WebDriver;


public class BrowserWebDriver {

    public static WebDriver createDefault() {
        return new BrowserWebDriverBuilder().build();
    }

    public static BrowserWebDriverBuilder custom() {
        return new BrowserWebDriverBuilder();
    }


}

