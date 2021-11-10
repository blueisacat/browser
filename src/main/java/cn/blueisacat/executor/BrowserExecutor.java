package cn.blueisacat.executor;

/**
 * @Title: BrowserExecutor
 * @author: gc
 * @Description: BrowserExecutor
 */
public class BrowserExecutor<IN, OUT> {

    public BrowserExecutorResult<OUT> execute(BrowserExecutorFunction<IN, OUT> function, IN in) {
        return function.invoke(in);
    }

}
