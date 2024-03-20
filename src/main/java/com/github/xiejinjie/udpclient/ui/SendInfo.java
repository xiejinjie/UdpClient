package com.github.xiejinjie.udpclient.ui;

import com.github.xiejinjie.udpclient.util.SocketUtil;

/**
 * ProcessDocument
 *
 * @author xiejinjie
 * @date 2023/12/23
 */
public class SendInfo {
    private String host;

    private Integer port;
    /**
     * 发送字节内容
     */
    private byte[] sendBytes;


    private Boolean hasReturn;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public byte[] getSendBytes() {
        return sendBytes;
    }

    public void setSendBytes(byte[] sendBytes) {
        this.sendBytes = sendBytes;
    }

    public Boolean getHasReturn() {
        return hasReturn;
    }

    public void setHasReturn(Boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    @Override
    public String toString() {
        return "SendInfo{" +
                ", host=" + host +
                ", port=" + port +
                "sendBytes=" + SocketUtil.bytesToHexString(sendBytes) +
                ", hasReturn=" + hasReturn +
                '}';
    }
}
