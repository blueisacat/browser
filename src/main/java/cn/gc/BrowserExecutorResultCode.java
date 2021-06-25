package cn.gc;

/**
 * @author : gc
 * @date : 2021/6/25
 */
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
