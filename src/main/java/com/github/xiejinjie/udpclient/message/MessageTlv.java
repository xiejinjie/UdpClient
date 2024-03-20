package com.github.xiejinjie.udpclient.message;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * TLV
 *
 * @author xiejinjie
 * @date 2024/3/11
 */
public class MessageTlv {
    private Integer tag;

    private Integer length;

    private byte[] value;

    public MessageTlv() {
    }

    public MessageTlv(int tag, byte[] value) {
        this.tag = tag;
        this.value = value;
        this.length = value.length;
    }

    public MessageTlv(int tag, String value) {
        byte[] bytes;
        if (value != null) {
            bytes = value.getBytes(StandardCharsets.US_ASCII);
        } else {
            bytes = new byte[]{};
        }
        this.tag = tag;
        this.value = bytes;
        this.length = bytes.length;
    }

    public MessageTlv(int tag, int value, int len) {
        byte[] bytes = MessageUtil.intToByte(value, len);
        this.tag = tag;
        this.value = bytes;
        this.length = bytes.length;
    }

    public MessageTlv(int tag, List<MessageTlv> tlvList) {
        byte[] bytes = MessageUtil.convertTlvListToByteArray(tlvList);
        this.tag = tag;
        this.value = bytes;
        this.length = bytes.length;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MessageTLV{" +
                "tag=" + tag +
                ", length=" + length +
                ", value=" + MessageUtil.convertByteArrayToHexStr(value) +
                '}';
    }
}
