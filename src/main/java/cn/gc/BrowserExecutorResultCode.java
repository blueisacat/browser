package cn.gc;

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
