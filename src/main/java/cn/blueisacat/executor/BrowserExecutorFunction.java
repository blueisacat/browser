package cn.blueisacat.executor;

import cn.blueisacat.browser.Browser;
import cn.blueisacat.pool.BrowserPool;

/**
 * @Title: BrowserExecutorFunction
 * @author: gc
 * @Description: BrowserExecutorFunction
 */
public abstract class BrowserExecutorFunction<IN, OUT> {

    public BrowserExecutorResult<OUT> invoke(IN in) {
        Browser browser = BrowserPool.getInstance().getBrowser();
        try {
            return doBusiness(browser, in);
        } catch (Exception e) {
            browser.destroy();
            return new BrowserExecutorResult<OUT>();
        } finally {
            browser.release();
        }
    }

    protected abstract BrowserExecutorResult<OUT> doBusiness(Browser browser, IN in) throws Exception;

}
