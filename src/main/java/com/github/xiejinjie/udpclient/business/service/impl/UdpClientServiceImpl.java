package com.github.xiejinjie.udpclient.business.service.impl;

import com.github.xiejinjie.udpclient.business.service.IUdpClientService;
import com.github.xiejinjie.udpclient.common.CommonThreadPool;
import com.github.xiejinjie.udpclient.core.UdpClient;
import com.github.xiejinjie.udpclient.ui.SendInfo;
import com.github.xiejinjie.udpclient.util.SocketUtil;
import com.github.xiejinjie.udpclient.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;

/**
 * UdpClientServiceImpl
 *
 * @author xiejinjie
 * @date 2024/3/16
 */
@Service
public class UdpClientServiceImpl implements IUdpClientService {
    private static final Logger logger = LoggerFactory.getLogger(UdpClientServiceImpl.class);
    @Autowired
    private UdpClient udpClient;

    @Override
    public CompletableFuture<byte[]> sendUdpPacket(SendInfo sendInfo) {
        return sendUdpPacket(sendInfo.getHost(), sendInfo.getPort(), sendInfo.getSendBytes(), sendInfo.getHasReturn());
    }

    private CompletableFuture<byte[]> sendUdpPacket(String host, int port, byte[] msg, boolean hasReturn) {
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
            if (hasReturn) {
                CompletableFuture<byte[]> future = new CompletableFuture<>();
                CommonThreadPool.threadPool.execute(()-> {
                    byte[] returnMsg = {};
                    try {
                        byte[] backBuf = new byte[1024];
                        DatagramPacket backPacket = new DatagramPacket(backBuf, backBuf.length);
                        socket.setSoTimeout(10000);
                        socket.receive(backPacket);
                        returnMsg = SocketUtil.subBytes(backBuf, 0, backPacket.getLength());
                        logger.info("Receive udp packet. msg={}", SocketUtil.bytesToHexString(returnMsg));
                    } catch (SocketTimeoutException ste) {
                        logger.warn("Receive udp packet timeout.");
                    } catch (Exception e) {
                        logger.error("Fail to send udp packet.", e);
                    }
                    future.complete(returnMsg);
                });
                return future;
            }
        } catch (Exception e) {
            logger.error("Fail to send udp packet.", e);
        }
        return null;
    }
}
