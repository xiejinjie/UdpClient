package com.github.xiejinjie.udpclient.message;

import com.github.xiejinjie.udpclient.util.SocketUtil;
import com.github.xiejinjie.udpclient.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * MessageUtil
 *
 * @author xiejinjie
 * @date 2024/3/11
 */
public class MessageUtil {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    /**
     * 消息解析
     *
     * @param msgBytes 消息字节数组
     * @return 消息对象
     */
    public static Message parseMessage(byte[] msgBytes) {
        int index = 0;
        Message message = new Message();
        // type
        message.setType(msgBytes[index] & 0xFF);
        index += MessageConstant.MSG_TYPE_LEN;
        // request id
        message.setRequestId(SocketUtil.bytesToHexString(msgBytes, index, MessageConstant.MSG_REQUEST_ID_LEN));
        index += MessageConstant.MSG_REQUEST_ID_LEN;
        // len
        int len = byteArrayToInt(msgBytes, index, MessageConstant.MSG_TLV_PART_LEN);
        message.setLen(len);
        index += MessageConstant.MSG_TLV_PART_LEN;
        // tlv
        List<MessageTlv> tlvList = parseTlvList(msgBytes, index, len);
        message.setTlvList(tlvList);
        return message;
    }

    /**
     * 消息转换为字节数组
     *
     * @param message 消息对象
     * @return 字节数组
     */
    public static byte[] convertMessageToByteArray(Message message) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // type
            if (message.getType() != null) {
                baos.write(message.getType().byteValue());
            } else {
                baos.write(0);
            }
            // request id
            if (message.getRequestId() != null) {
                baos.write(SocketUtil.hexStringToBytes(message.getRequestId()));
            } else {
                baos.write(new byte[]{0,0,0,0});
            }
            // 响应消息字段
            if (message instanceof MessageResp) {
                MessageResp msgRsp = (MessageResp) message;
                if (msgRsp.getStatus() != null) {
                    baos.write(msgRsp.getStatus());
                } else {
                    baos.write(MessageConstant.RESP_STATUS_OTHER_ERROR);
                }
                if (msgRsp.getRequestType() != null) {
                    baos.write(msgRsp.getRequestType());
                } else {
                    baos.write(0);
                }
            }
            // len
            int len = message.getLen();
            baos.write((byte) ((len >> 8) & 0xFF));
            baos.write((byte) len);
            // tlv
            for (MessageTlv tlv : message.getTlvList()) {
                baos.write(tlv.getTag().byteValue());
                baos.write(tlv.getLength().byteValue());
                baos.write(tlv.getValue());
            }
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("消息转换为字节数组异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 消息转换为字节数组
     *
     * @param tlvList 消息对象
     * @return 字节数组
     */
    public static byte[] convertTlvListToByteArray(List<MessageTlv> tlvList) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (MessageTlv tlv : tlvList) {
                baos.write(tlv.getTag().byteValue());
                baos.write(tlv.getLength().byteValue());
                baos.write(tlv.getValue());
            }
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("消息转换为字节数组异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 从字节数组中解析TLV
     *
     * @param msgBytes 字节数组
     * @return 解析后TLV内容
     */
    public static List<MessageTlv> parseTlvList(byte[] msgBytes) {
        return parseTlvList(msgBytes, 0, msgBytes.length);
    }

    /**
     * 从字节数组中解析TLV
     *
     * @param msgBytes 字节数组
     * @param offset TLV内容起始位置偏移量
     * @param len TLV内容长度
     * @return 解析后TLV内容
     */
    public static List<MessageTlv> parseTlvList(byte[] msgBytes, int offset, int len) {
        int index = offset;
        List<MessageTlv> tlvList = new ArrayList<>();
        while (index < offset + len ) {
            MessageTlv tlv = new MessageTlv();
            // tlv tag
            tlv.setTag(msgBytes[index] & 0xff);
            index += MessageConstant.MSG_TLV_TAG_LEN;
            // tlv len (value)
            int valueLen = msgBytes[index] & 0xff;
            index += MessageConstant.MSG_TLV_VALUE_LEN;
            tlv.setLength(valueLen);
            // tlv value
            tlv.setValue(SocketUtil.subBytes(msgBytes, index, valueLen));
            index += valueLen;

            tlvList.add(tlv);
        }
        return tlvList;
    }

    /**
     * 根据标签值从tlv列表中获取value内容
     * @param tag 标签值
     * @param tlvList tlv列表
     * @return value内容(二进制数组)
     */
    public static byte[] getValueFromTlvList(int tag, List<MessageTlv> tlvList) {
        if (StringUtils.isEmpty(tlvList)) {
            return null;
        }
        for (MessageTlv tlv : tlvList) {
            if (tag == tlv.getTag()) {
                return tlv.getValue();
            }
        }
        return null;
    }

    /**
     * 根据标签值从tlv列表中获取value内容列表（有多个相同标签）
     * @param tag 标签值
     * @param tlvList tlv列表
     * @return value内容(二进制数组)
     */
    public static List<byte[]> getValuesFromTlvList(int tag, List<MessageTlv> tlvList) {
        if (StringUtils.isEmpty(tlvList)) {
            return Collections.emptyList();
        }
        List<byte[]> list = new ArrayList<>();
        for (MessageTlv tlv : tlvList) {
            if (tag == tlv.getTag()) {
                list.add(tlv.getValue());
            }
        }
        return list;
    }

    /**
     * 字节数组转成字符串
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String convertByteArrayToStr(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    /**
     * 字节数组转换成十六进制字符
     * @param bytes 字节数组
     * @return 十六进制字符
     */
    public static String convertByteArrayToHexStr(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null) {
            return null;
        }
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static Integer convertByteArrayToInteger(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return byteArrayToInt(bytes);
    }

    /**
     * 字节数组转int 大端模式
     */
    public static int byteArrayToInt(byte[] bytes) {
        if (bytes.length > 4) {
            throw new IllegalArgumentException("The length of byte array is exceed the length of integer.");
        }
        int intValue = 0;
        for (byte aByte : bytes) {
            intValue <<= 8;
            int b = aByte & 0xFF;
            intValue |= b;
        }
        return intValue;
    }

    /**
     * 字节数组转int 大端模式
     */
    public static int byteArrayToInt(byte[] bytes, int byteOffset, int byteCount) {
        int intValue = 0;
        for (int i = byteOffset; i < (byteOffset + byteCount); i++) {
            intValue <<= 8;
            int b = bytes[i] & 0xFF;
            intValue |= b;
        }
        return intValue;
    }

    public static byte[] intToByte(int x) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (x >> 24);
        bytes[1] = (byte) (x >> 16);
        bytes[2] = (byte) (x >> 8);
        bytes[3] = (byte) x;
        return bytes;
    }

    public static byte[] intToByte(int x, int len) {
        if (len < 1 || len > 4) {
            throw new IllegalArgumentException("Invalid len.");
        }
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (x >> ((len - 1 - i) * 8));
        }
        return bytes;
    }

    /**
     * 判断两个ipv6地址是否相同（针对同一个ipv6有多种不同形式）
     * @param ip1 ip1
     * @param ip2 ip2
     * @return 是否相同
     */
    public static boolean isSameIpv6(String ip1, String ip2) {
        if (StringUtils.isNotEmpty(ip1) && StringUtils.isNotEmpty(ip2)) {
            return padIpv6(ip1).equals(padIpv6(ip2));
        }
        return false;
    }

    /**
     * 将ipv6补充为完整地址
     */
    public static String padIpv6(String ipv6) {
        String[] split = ipv6.split(":");
        int empty = -1;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.length() == 0 && empty ==-1) {
                empty = i;
            } else if (s.length() < 4){
                StringBuilder sb = new StringBuilder(4);
                for (int j = 0; j < 4 - s.length(); j++) {
                    sb.append('0');
                }
                sb.append(s);
                split[i] = sb.toString();
            }
        }
        if (empty != -1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8 - split.length + 1; i++){
                sb.append("0000:");
            }
            split[empty] = sb.substring(0, sb.length() - 1);
        }
        return String.join(":", split);
    }

    /**
     * 判断地址是否ipv6
     */
    public static boolean isIpv6(String host) {
        try {
            // v6地址补齐
            InetAddress inetAddress = InetAddress.getByName(host);
            if (inetAddress instanceof java.net.Inet6Address) {
                return true;
            }
        } catch (UnknownHostException e) {
            logger.warn("未知的ip地址", e);
        }
        return false;
    }

}
