package cn.blueisacat.executor.impl;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.executor.BrowserExecutorFunction;
import cn.blueisacat.executor.BrowserExecutorResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: FetchPageSourceFunction
 * @author: gc
 * @Description: FetchPageSourceFunction
 */
public class FetchPageSourceFunction<Request, Response> extends BrowserExecutorFunction<FetchPageSourceFunction.Request, FetchPageSourceFunction.Response> {

    @Override
    protected BrowserExecutorResult<FetchPageSourceFunction.Response> doBusiness(Browser browser, FetchPageSourceFunction.Request request) throws Exception {
        browser.setTimeout(request.getTimeoutInSecond());
        browser.getWebDriver().get(request.getUrl());
        Thread.sleep(request.getWaitInMillisecond());
        String pageSource = browser.getWebDriver().getPageSource();
        String currentUrl = browser.getWebDriver().getCurrentUrl();
        List<String> iframePageSources = browser.getWebDriver().findElements(By.cssSelector("iframe")).stream().map(iframe -> {
            String iframePageSource = null;
            try {
                WebDriver webDriver = browser.getWebDriver().switchTo().frame(iframe);
                Thread.sleep(request.getWaitInMillisecond());
                iframePageSource = webDriver.getPageSource();
                browser.getWebDriver().switchTo().defaultContent();
            } catch (Exception e) {
            }
            return iframePageSource;
        }).filter(iframePageSource -> !Strings.isNullOrEmpty(iframePageSource)).collect(Collectors.toList());
        return new BrowserExecutorResult<FetchPageSourceFunction.Response>(new FetchPageSourceFunction.Response(currentUrl, pageSource, iframePageSources));
    }

    public static class Request {
        private static final long MIN_WAIT_IN_MILLISECOND = 0L;
        private static final long MAX_WAIT_IN_MILLISECOND = 10_000L;
        private static final long DEFAULT_WAIT_IN_MILLISECOND = 1_000L;
        private static final long MIN_TIMEOUT_IN_SECOND = 10L;
        private static final long MAX_TIMEOUT_IN_SECOND = 120L;
        private static final long DEFAULT_TIMEOUT_IN_SECOND = 60L;

        private String url;
        private long timeoutInSecond = DEFAULT_TIMEOUT_IN_SECOND;
        private long waitInMillisecond = DEFAULT_WAIT_IN_MILLISECOND;

        public Request(String url) {
            this.url = url;
        }

        public Request(String url, long timeoutInSecond, long waitInMillisecond) {
            this.url = url;
            this.timeoutInSecond = Longs.constrainToRange(timeoutInSecond, MIN_TIMEOUT_IN_SECOND, MAX_TIMEOUT_IN_SECOND);
            this.waitInMillisecond = Longs.constrainToRange(waitInMillisecond, MIN_WAIT_IN_MILLISECOND, MAX_WAIT_IN_MILLISECOND);
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getTimeoutInSecond() {
            return timeoutInSecond;
        }

        public void setTimeoutInSecond(long timeoutInSecond) {
            this.timeoutInSecond = timeoutInSecond;
        }

        public long getWaitInMillisecond() {
            return waitInMillisecond;
        }

        public void setWaitInMillisecond(long waitInMillisecond) {
            this.waitInMillisecond = waitInMillisecond;
        }
    }

    public static class Response {

        private String currentUrl;

        private String pageSource;

        private List<String> iframePageSources = Lists.newArrayList();

        public Response(String currentUrl, String pageSource, List<String> iframePageSources) {
            this.currentUrl = currentUrl;
            this.pageSource = pageSource;
            this.iframePageSources = iframePageSources;
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

        public List<String> getIframePageSources() {
            return iframePageSources;
        }

        public void setIframePageSources(List<String> iframePageSources) {
            this.iframePageSources = iframePageSources;
        }
    }

}
