package cn.blueisacat;

import java.io.Serializable;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public class BrowserExecutorResult implements Serializable {

  private static final long serialVersionUID = 1L;

  private BrowserExecutorResultCode code;

  private String currentUrl;

  private String pageSource;

  private String msg;

  private BrowserExecutorResult() {}

  public BrowserExecutorResultCode getCode() {
    return code;
  }

  public void setCode(BrowserExecutorResultCode code) {
    this.code = code;
  }

  public String getCurrentUrl() {
    return currentUrl;
  }

  public void setCurrentUrl(String currentUrl) {
    this.currentUrl = currentUrl;
  }

  public String getPageSource() {
    return pageSource;
  }

  public void setPageSource(String pageSource) {
    this.pageSource = pageSource;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public boolean isSuccess() {
    if (null != this.code) {
      return this.code == BrowserExecutorResultCode.SUCCESS ? true : false;
    } else {
      return false;
    }
  }

  public boolean isFailure() {
    if (null != this.code) {
      return this.code == BrowserExecutorResultCode.Failure ? true : false;
    } else {
      return true;
    }
  }

  public static BrowserExecutorResult success(String msg, String pageSource, String currentUrl) {
    BrowserExecutorResult browserExecutorResult = new BrowserExecutorResult();
    browserExecutorResult.setCode(BrowserExecutorResultCode.SUCCESS);
    browserExecutorResult.setMsg(msg);
    browserExecutorResult.setPageSource(pageSource);
    browserExecutorResult.setCurrentUrl(currentUrl);
    return browserExecutorResult;
  }

  public static BrowserExecutorResult success(String pageSource, String currentUrl) {
    return success("", pageSource, currentUrl);
  }

  public static BrowserExecutorResult failure(String msg) {
    BrowserExecutorResult browserExecutorResult = new BrowserExecutorResult();
    browserExecutorResult.setCode(BrowserExecutorResultCode.Failure);
    browserExecutorResult.setMsg(msg);
    return browserExecutorResult;
  }

  public static BrowserExecutorResult failure() {
    return failure("");
  }

}
