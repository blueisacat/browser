package cn.blueisacat;

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

  private static long validateTimeoutInSecond(long timeoutInSecond) {
    if (timeoutInSecond < MIN_TIMEOUT_IN_SECOND) {
      timeoutInSecond = MIN_TIMEOUT_IN_SECOND;
    } else if (timeoutInSecond > MAX_TIMEOUT_IN_SECOND) {
      timeoutInSecond = MAX_TIMEOUT_IN_SECOND;
    }
    return timeoutInSecond;
  }

}
