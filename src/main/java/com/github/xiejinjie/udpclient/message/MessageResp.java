package com.github.xiejinjie.udpclient.message;

/**
 * 通用响应消息
 *
 * @author xiejinjie
 * @date 2024/3/11
 */
public class MessageResp extends Message{
    private Integer status;

    private Integer requestType;

    public MessageResp() {
    }

    public MessageResp(String requestId, int requestType, int status) {
        this.type = MessageConstant.MSG_TYPE_RESP;
        this.requestId = requestId;
        this.requestType = requestType;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "MessageResp{" +
                "type=" + type +
                ", requestId='" + requestId + '\'' +
                ", len=" + len +
                ", status=" + status +
                ", requestType=" + requestType +
                ", tlvList=" + tlvList +
                '}';
    }
}
