package cn.blueisacat.executor;

/**
 * @Title: BrowserExecutorResult
 * @author: gc
 * @Description: BrowserExecutorResult
 */
public class BrowserExecutorResult<OUT> {

    public BrowserExecutorResult() {

    }

    public BrowserExecutorResult(OUT data) {
        this.data = data;
    }

    private OUT data;

    public OUT get() {
        return data;
    }

}
