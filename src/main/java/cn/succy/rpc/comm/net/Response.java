package cn.succy.rpc.comm.net;

/**
 * @author Succy
 * @date 2017/2/20 17:34
 */
public class Response {
    private String requestId;
    private int respCode;
    private String msg;
    private Object data;
    // getter/setter
    public Response() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "requestId='" + requestId + '\'' +
                ", respCode=" + respCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
