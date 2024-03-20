package com.github.xiejinjie.udpclient.message;

import java.util.List;

/**
 * 通用请求消息
 *
 * @author xiejinjie
 * @date 2024/3/11
 */
public class Message {
    protected Integer type;

    protected String requestId;

    protected Integer len;

    protected List<MessageTlv> tlvList;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public List<MessageTlv> getTlvList() {
        return tlvList;
    }

    public void setTlvList(List<MessageTlv> tlvList) {
        this.tlvList = tlvList;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", requestId='" + requestId + '\'' +
                ", len=" + len +
                ", tlvList=" + tlvList +
                '}';
    }
}
