package ysu.edu.util;

public enum ResponseState {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    NO_SESSION(401, "未登录或登录超时"),
    NO_ACCESS(403, "无操作权限");

    private int code;
    private String msg;

    ResponseState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
