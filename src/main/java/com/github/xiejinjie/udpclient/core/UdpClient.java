package com.github.xiejinjie.udpclient.core;

import com.github.xiejinjie.udpclient.message.Message;
import com.github.xiejinjie.udpclient.message.MessageBuilder;
import com.github.xiejinjie.udpclient.message.MessageTlv;
import com.github.xiejinjie.udpclient.message.MessageUtil;
import com.github.xiejinjie.udpclient.util.JsonUtil;
import com.github.xiejinjie.udpclient.util.SocketUtil;
import com.github.xiejinjie.udpclient.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test.UdpClient
 * 编写一个UdpClient,支持界面和指令行操作
 * 可以向指定ip和端口，可以设置UDP包消息体，设置UDP包的个数和间隔，有返回和无返回
 *
 * @author xiejinjie
 * @date 2024/3/7
 */
@Component
public class UdpClient {
    private static Logger logger = LoggerFactory.getLogger(UdpClient.class);

    public static void main(String[] args) throws InterruptedException {
        byte[] msg = buildTaskQueryMsg();
        System.out.println(SocketUtil.bytesToHexString(msg));
        sendUdpPacket("127.0.0.1", 10808, msg);

        Thread.sleep(1000);

        msg = buildTaskNotifyMsg();
        System.out.println(SocketUtil.bytesToHexString(msg));
        sendUdpPacket("127.0.0.1", 10808, msg);
    }

    public static void buildMsg() {
        // 构造消息，十六进制输出和二进制输出
        // 数字、字符串、模板
    }

    public static byte[] buildActiveMsg() {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setType(30);
        messageBuilder.setRequestId("00000001");
        messageBuilder.addTlv(30, "123456789");
        Map<String, Object> map = new HashMap<>();
        System.out.println(JsonUtil.writeValueAsString(map).length());
        messageBuilder.addTlv(38, "192.168.101.2/26");
        messageBuilder.addTlv(39, "192.168.101.4/24");
        messageBuilder.addTlv(40, "192.168.101.1/20");
        Message msg = messageBuilder.build();
        return MessageUtil.convertMessageToByteArray(msg);
    }

    public static byte[] buildOnLineMsg() {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setType(31);
        messageBuilder.setRequestId("00000001");
        messageBuilder.addTlv(35, 0, 1);
        messageBuilder.addTlv(36, "127.0.0.1");
        messageBuilder.addTlv(30, "12345678");
        Message msg = messageBuilder.build();
        return MessageUtil.convertMessageToByteArray(msg);
    }

    public static byte[] buildConfigQueryMsg() {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setType(36);
        messageBuilder.setRequestId("00000001");
        messageBuilder.addTlv(30, "CESHI_1");
        Message msg = messageBuilder.build();
        return MessageUtil.convertMessageToByteArray(msg);
    }

    public static byte[] buildReportMsg() {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setType(20);
        messageBuilder.setRequestId("00000001");
        messageBuilder.addTlv(30, "123456789");
        messageBuilder.addTlv(32, "192.168.101.100");
        messageBuilder.addTlv(49, "AA::CC");

        Message msg = messageBuilder.build();
        return MessageUtil.convertMessageToByteArray(msg);
    }

    public static byte[] buildTaskQueryMsg() {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setType(38);
        messageBuilder.setRequestId("00000001");
        messageBuilder.addTlv(30, "CPE-20204021528");

        Message msg = messageBuilder.build();
        return MessageUtil.convertMessageToByteArray(msg);
    }

    public static byte[] buildTaskNotifyMsg() {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setType(39);
        messageBuilder.setRequestId("00000001");
        messageBuilder.addTlv(30, "CPE-20204021528");
        List<MessageTlv> list = new ArrayList<>();
        list.add(new MessageTlv(12, 2, 1));
        list.add(new MessageTlv(1, 0, 1));
        messageBuilder.addTlv(10, MessageUtil.convertTlvListToByteArray(list));

        Message msg = messageBuilder.build();
        return MessageUtil.convertMessageToByteArray(msg);
    }

    public static void sendUdpPacket(String host, int port, byte[] msg) {
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("The host cannot be empty.");
        }
        try {
            logger.info("Sending udp packet. host={}, port={}, msg={}",
                    host, port, SocketUtil.bytesToHexString(msg));
            // build udp packet
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket dataGramPacket = new DatagramPacket(msg, msg.length, address, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(dataGramPacket);
            // waiting for the reply
            byte[] backBuf = new byte[1024];
            DatagramPacket backPacket = new DatagramPacket(backBuf, backBuf.length);
            socket.receive(backPacket);
            logger.info("The back packet is {}", SocketUtil.bytesToHexString(backBuf, 0, backPacket.getLength()));
            logger.info("The back packet in string is {}", new String(backBuf, 0, backPacket.getLength()));
        } catch (Exception e) {
            logger.error("Fail to send udp packet.", e);
        }
    }
}
