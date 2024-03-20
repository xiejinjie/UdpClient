package com.github.xiejinjie.udpclient.business.service;

import com.github.xiejinjie.udpclient.ui.SendInfo;

import java.util.concurrent.CompletableFuture;

/**
 * IUdpClientService
 *
 * @author xiejinjie
 * @date 2024/3/16
 */
public interface IUdpClientService {

    CompletableFuture<byte[]> sendUdpPacket(SendInfo sendInfo);


}
