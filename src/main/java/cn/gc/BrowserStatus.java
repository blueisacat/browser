package cn.gc;

/**
 * @author : gc
 * @date : 2021/6/25
 */
public enum BrowserStatus {
  NOT_USE(0), // 未使用
  IN_USE(1), // 使用中
  DESTROY(2); // 已销毁

  private int value;

  private BrowserStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
