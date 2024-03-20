package com.github.xiejinjie.udpclient.message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageBuilder
 *
 * @author xiejinjie
 * @date 2024/3/11
 */
public class MessageBuilder {
    protected Message message;

    public MessageBuilder() {
        this.message = new Message();
        this.message.setRequestId(generateRequestId());
        this.message.setTlvList(new ArrayList<>());
    }

    public MessageBuilder randomRequestId() {
        this.message.setRequestId(generateRequestId());
        return this;
    }

    public MessageBuilder setRequestId(String requestId) {
        this.message.setRequestId(requestId);
        return this;
    }

    public MessageBuilder setType(int type) {
        this.message.setType(type);
        return this;
    }

    public MessageBuilder addTlv(int tag, byte[] value) {
        this.message.getTlvList().add(new MessageTlv(tag, value));
        return this;
    }

    public MessageBuilder addTlv(int tag, String value) {
        byte[] bytes;
        if (value != null) {
            bytes = value.getBytes(StandardCharsets.US_ASCII);
        } else {
            bytes = new byte[]{};
        }
        this.message.getTlvList().add(new MessageTlv(tag, bytes));
        return this;
    }

    /**
     * 添加TLV属性
     *
     * @param tag 标签
     * @param value 数值
     * @param len 字段长度
     */
    public MessageBuilder addTlv(int tag, int value, int len) {
        byte[] bytes = MessageUtil.intToByte(value, len);
        this.message.getTlvList().add(new MessageTlv(tag, bytes));
        return this;
    }

    /**
     * 添加TLV属性 嵌套TLV
     *
     * @param tag 标签
     * @param tlvList 数值
     */
    public MessageBuilder addTlv(int tag, List<MessageTlv> tlvList) {
        byte[] bytes = MessageUtil.convertTlvListToByteArray(tlvList);
        this.message.getTlvList().add(new MessageTlv(tag, bytes));
        return this;
    }

    public Message build() {
        // len
        int len = 0;
        for (MessageTlv tlv : this.message.getTlvList()) {
            len += tlv.getLength();
        }
        this.message.setLen(len);
        return this.message;
    }

    protected String generateRequestId() {
        String s = String.valueOf(System.currentTimeMillis());
        return s.substring(s.length() - 8);
    }
}
