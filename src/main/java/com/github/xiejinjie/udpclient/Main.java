package com.github.xiejinjie.udpclient;

import com.github.xiejinjie.udpclient.util.SocketUtil;

/**
 * com.github.xiejinjie.udpclient.Main
 *
 * @author xiejinjie
 * @date 2024/3/18
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(new String(SocketUtil.hexStringToBytes("746573745f6370655f6e616d65")));
        System.out.println(SocketUtil.byteArrayToInt(SocketUtil.hexStringToBytes("1f90"), 0, 2));
    }
}
