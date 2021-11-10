package cn.blueisacat.executor;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

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

    private List<String> iframePageSources = Lists.newArrayList();

    private BrowserExecutorResult() {
    }

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

    public List<String> getIframePageSources() {
        return iframePageSources;
    }

    public void setIframePageSources(List<String> iframePageSources) {
        this.iframePageSources = iframePageSources;
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

    public static BrowserExecutorResult success(String msg, String pageSource, String currentUrl, List<String> iframePageSources) {
        BrowserExecutorResult browserExecutorResult = new BrowserExecutorResult();
        browserExecutorResult.setCode(BrowserExecutorResultCode.SUCCESS);
        browserExecutorResult.setMsg(msg);
        browserExecutorResult.setPageSource(pageSource);
        browserExecutorResult.setCurrentUrl(currentUrl);
        browserExecutorResult.setIframePageSources(iframePageSources);
        return browserExecutorResult;
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

    public static BrowserExecutorResult success(String pageSource, String currentUrl, List<String> iframePageSources) {
        return success("", pageSource, currentUrl, iframePageSources);
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

    public enum BrowserExecutorResultCode {
        SUCCESS(0), Failure(1);

        private int value;

        private BrowserExecutorResultCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
