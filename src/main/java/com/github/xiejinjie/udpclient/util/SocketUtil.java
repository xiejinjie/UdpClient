package com.github.xiejinjie.udpclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketUtil {
    private static final Logger logger = LoggerFactory.getLogger(SocketUtil.class);

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] src, int offset, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (int i = offset; i < offset + count; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * int转字节数组 大端模式
     */
    public static byte[] intToByteArrayBigEndian(int x) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (x >> 24);
        bytes[1] = (byte) (x >> 16);
        bytes[2] = (byte) (x >> 8);
        bytes[3] = (byte) x;
        return bytes;
    }

    /**
     * int转字节数组 小端模式
     */
    public static byte[] intToByteArrayLittleEndian(int x) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) x;
        bytes[1] = (byte) (x >> 8);
        bytes[2] = (byte) (x >> 16);
        bytes[3] = (byte) (x >> 24);
        return bytes;
    }

    /**
     * 字节数组转int 小端模式
     */
    public static int byteArrayToIntLittleEndian(byte[] b, int byteOffset, int byteCount) {
        int intValue = 0;
        for (int i = byteOffset; i < (byteOffset + byteCount); i++) {
            intValue += (b[i] & 0xFF) << (8 * (i - byteOffset));
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

    /**
     * ipv6数组转字符串
     */
    public static String byteArrayToIpv6String(byte[] bytes, int offset) {
        StringBuilder sb = new StringBuilder(39);
        for (int i = 0; i < 8; i++) {
            int tmp = (bytes[offset+(i<<1)]<<8 & 0xff00) | (bytes[offset+(i<<1)+1] & 0xff);
            sb.append(Integer.toHexString(tmp));
            if (i < 7) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    public static boolean isSameIpv6(String ip1, String ip2) {
        if (StringUtils.isNotEmpty(ip1) && StringUtils.isNotEmpty(ip2)) {
            return padIpv6(ip1).equals(padIpv6(ip2));
        }
        return false;
    }

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

    public static boolean isIpv6(String host) {
        try {
            // v6地址补齐
            InetAddress inetAddress = InetAddress.getByName(host);
            if (inetAddress instanceof java.net.Inet6Address) {
                return true;
            }
        } catch (UnknownHostException e) {
            logger.warn("ip地址失败", e);
        }
        return false;
    }


}
