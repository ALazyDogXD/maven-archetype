package __package__.common.base.resp;

/**
 * @author ALazyDogXD
 * @date 2021/9/20 15:55
 * @description 响应
 */

public enum ResponseStatus implements Response {
    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),
    /**
     * 服务异常
     */
    FAIL(500, "服务异常"),
    /**
     * 错误请求
     */
    ERROR(400, "错误请求");

    private final int code;

    private final String msg;

    ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
