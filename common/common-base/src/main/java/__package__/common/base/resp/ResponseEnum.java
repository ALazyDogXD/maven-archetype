package __package__.common.base.resp;

/**
 * @author ALazyDogXD
 * @date 2021/9/20 15:55
 * @description 响应
 */

public enum ResponseEnum implements Response {
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

//    // ============== EDU:1100 ==============
//    INVALID_COURSE_ID(1100, "无效的课程 ID"),
//    IMAGE_UPLOAD_FAIL(1101, "图片上传失败"),
//    COURSE_ADD_FAIL(1102, "课程添加失败"),
//    COURSE_DEL_FAIL(1103, "课程删除失败"),
//    IMAGE_SIZE_OUT_OF_RANGE(1104, "文件大小超范围"),
//    IMAGE_IS_NULL(1105, "请选择要上传的图片"),
//    IS_NOT_IMAGE(1106, "请选择图片文件上传"),
//    EXCEL_IS_NULL(1107, "请选择要上传的 excel 文件"),
//    IS_NOT_EXCEL(1108, "请选择 excel 文件上传"),
//    EXCEL_UPLOAD_FAIL(1109, "excel 文件上传失败"),
//    // ============== MINIO:1200 ==============
//    FILE_UPLOAD_FAIL(1200, "文件上传失败"),
//    FILE_DEL_FAIL(1201, "文件删除失败"),
//    FILE_NOT_FIND_FAIL(1202, "未查询到文件"),
//    // ============== VID:1300 ==============
//    VIDEO_UPLOAD_FAIL(1300, "视频上传失败"),
//    VIDEO_IS_NULL(1301, "请选择要上传的视频"),
//    // ============== SMS:1400 ==============
//    SEND_AUTH_CODE_FAIL(1400, "服务器异常, 验证码获取失败");

    private final int code;

    private final String msg;

    ResponseEnum(int code, String msg) {
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
