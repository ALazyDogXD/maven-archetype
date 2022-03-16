package __package__.common.base.resp;

/**
 * @author ALazyDogXD
 * @date 2021/9/20 15:53
 * @description 响应
 */

public class ResponseData<T> extends ResponseBase {

    private T data;

    public static <T> ResponseData<T> success(T data) {
        ResponseData<T> resp = new ResponseData<>();
        resp.code = 200;
        resp.data = data;
        return resp;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }
}
