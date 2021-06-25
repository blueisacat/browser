package cn.gc;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : gc
 * @date : 2021/6/25
 */
public class Browser {

  public Browser() {
    this.webDriver = BrowserWebDriver.createDefault();
    this.status = BrowserStatus.NOT_USE;
    this.startTime = new Date().getTime();
  }

  private org.openqa.selenium.WebDriver webDriver;

  private BrowserStatus status;

  private Long startTime;

  public org.openqa.selenium.WebDriver getWebDriver() {
    return webDriver;
  }

  public BrowserStatus getStatus() {
    return status;
  }

  public Long getStartTime() {
    return startTime;
  }

  public boolean test() {
    try {
      webDriver.getCurrentUrl();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void release() {
    this.status = BrowserStatus.NOT_USE;
  }

  public Browser get() {
    this.status = BrowserStatus.IN_USE;
    return this;
  }

  public Browser getWithTest() {
    if (!test()) {
      destroy();
      this.webDriver = BrowserWebDriver.createDefault();
    }
    this.status = BrowserStatus.IN_USE;
    return this;
  }

  public void destroy() {
    this.status = BrowserStatus.DESTROY;
    new Thread(() -> {
      if (null != this.webDriver) {
        try {
          this.webDriver.quit();
        } catch (Exception e) {
        }
      }
    }).start();
  }

  public void setTimeout(long timeoutInSecond) {
    if (null != this.webDriver) {
      this.webDriver.manage().timeouts().pageLoadTimeout(timeoutInSecond, TimeUnit.SECONDS);
      this.webDriver.manage().timeouts().implicitlyWait(timeoutInSecond, TimeUnit.SECONDS);
      this.webDriver.manage().timeouts().setScriptTimeout(timeoutInSecond, TimeUnit.SECONDS);
    }
  }

}
