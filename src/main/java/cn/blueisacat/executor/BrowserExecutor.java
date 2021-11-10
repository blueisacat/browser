package cn.blueisacat.executor;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.pool.BrowserPool;
import com.google.common.base.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public class BrowserExecutor {

    private static final long DEFAULT_WAIT_IN_MILLISECOND = 1_000L;
    private static final long MIN_TIMEOUT_IN_SECOND = 10L;
    private static final long MAX_TIMEOUT_IN_SECOND = 120L;
    private static final long DEFAULT_TIMEOUT_IN_SECOND = 60L;

    public static BrowserExecutorResult getPageSource(String url) {
        return getPageSource(url, DEFAULT_WAIT_IN_MILLISECOND, DEFAULT_TIMEOUT_IN_SECOND);
    }

    public static BrowserExecutorResult getPageSource(String url, long timeoutInSecond) {
        return getPageSource(url, DEFAULT_WAIT_IN_MILLISECOND, validateTimeoutInSecond(timeoutInSecond));
    }

    public static BrowserExecutorResult getPageSource(String url, long waitInMillisecond, long timeoutInSecond) {
        Browser browser = BrowserPool.getInstance().getBrowser();
        try {
            browser.setTimeout(validateTimeoutInSecond(timeoutInSecond));
            browser.getWebDriver().get(url);
            Thread.sleep(waitInMillisecond);
            String pageSource = browser.getWebDriver().getPageSource();
            String currentUrl = browser.getWebDriver().getCurrentUrl();
            return BrowserExecutorResult.success(pageSource, currentUrl);
        } catch (Exception e) {
            browser.destroy();
            return BrowserExecutorResult.failure();
        } finally {
            browser.release();
        }
    }

    public static BrowserExecutorResult getPageSourceIncludeIFrame(String url) {
        return getPageSourceIncludeIFrame(url, DEFAULT_WAIT_IN_MILLISECOND, DEFAULT_TIMEOUT_IN_SECOND);
    }

    public static BrowserExecutorResult getPageSourceIncludeIFrame(String url, long timeoutInSecond) {
        return getPageSourceIncludeIFrame(url, DEFAULT_WAIT_IN_MILLISECOND, validateTimeoutInSecond(timeoutInSecond));
    }

    public static BrowserExecutorResult getPageSourceIncludeIFrame(String url, long waitInMillisecond, long timeoutInSecond) {
        Browser browser = BrowserPool.getInstance().getBrowser();
        browser.setTimeout(validateTimeoutInSecond(timeoutInSecond));
        browser.getWebDriver().get(url);
        try {
            browser.setTimeout(validateTimeoutInSecond(timeoutInSecond));
            browser.getWebDriver().get(url);
            Thread.sleep(waitInMillisecond);
            String pageSource = browser.getWebDriver().getPageSource();
            String currentUrl = browser.getWebDriver().getCurrentUrl();
            List<String> iframePageSources = browser.getWebDriver().findElements(By.cssSelector("iframe")).stream().map(iframe -> {
                String iframePageSource = null;
                try {
                    WebDriver webDriver = browser.getWebDriver().switchTo().frame(iframe);
                    Thread.sleep(waitInMillisecond);
                    iframePageSource = webDriver.getPageSource();
                    browser.getWebDriver().switchTo().defaultContent();
                } catch (Exception e) {
                }
                return iframePageSource;
            }).filter(iframePageSource -> !Strings.isNullOrEmpty(iframePageSource)).collect(Collectors.toList());
            return BrowserExecutorResult.success(pageSource, currentUrl, iframePageSources);
        } catch (Exception e) {
            browser.destroy();
            return BrowserExecutorResult.failure();
        } finally {
            browser.release();
        }

    }

    private static long validateTimeoutInSecond(long timeoutInSecond) {
        if (timeoutInSecond < MIN_TIMEOUT_IN_SECOND) {
            timeoutInSecond = MIN_TIMEOUT_IN_SECOND;
        } else if (timeoutInSecond > MAX_TIMEOUT_IN_SECOND) {
            timeoutInSecond = MAX_TIMEOUT_IN_SECOND;
        }
        return timeoutInSecond;
    }

}
