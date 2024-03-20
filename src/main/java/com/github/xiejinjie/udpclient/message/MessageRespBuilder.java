package com.github.xiejinjie.udpclient.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * MessageBuilder
 *
 * @author xiejinjie
 * @date 2024/3/11
 */
public class MessageRespBuilder extends MessageBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MessageRespBuilder.class);

    public MessageRespBuilder() {
        this.message = new MessageResp();
        this.message.setType(0x63);
        this.message.setRequestId(generateRequestId());
        this.message.setTlvList(new ArrayList<>());
    }

    public MessageRespBuilder setStatus(int status) {
        ((MessageResp) this.message).setStatus(status);
        return this;
    }

    public MessageRespBuilder setRequestIdTypeStatus(String requestId, int requestType, int status) {
        this.message.setRequestId(requestId);
        ((MessageResp) this.message).setRequestType(requestType);
        ((MessageResp) this.message).setStatus(status);
        return this;
    }

    @Override
    public MessageResp build() {
        return (MessageResp) super.build();
    }

    public MessageRespBuilder success() {
        return setStatus(MessageConstant.RESP_STATUS_SUCCESS);
    }

    public MessageRespBuilder argError() {
        return setStatus(MessageConstant.RESP_STATUS_INVALID_ARG);
    }

    public MessageRespBuilder error() {
        return setStatus(MessageConstant.RESP_STATUS_ERROR);
    }

    public MessageRespBuilder otherError() {
        return setStatus(MessageConstant.RESP_STATUS_OTHER_ERROR);
    }

    public MessageRespBuilder success(String requestId, int requestType) {
        return setRequestIdTypeStatus(requestId, requestType, MessageConstant.RESP_STATUS_SUCCESS);
    }

    public MessageRespBuilder argError(String requestId, int requestType) {
        return setRequestIdTypeStatus(requestId, requestType, MessageConstant.RESP_STATUS_INVALID_ARG);
    }

    public MessageRespBuilder error(String requestId, int requestType) {
        return setRequestIdTypeStatus(requestId, requestType, MessageConstant.RESP_STATUS_ERROR);
    }

    public MessageRespBuilder otherError(String requestId, int requestType) {
        return setRequestIdTypeStatus(requestId, requestType, MessageConstant.RESP_STATUS_OTHER_ERROR);
    }

    @Override
    public MessageRespBuilder setType(int type) {
        // 响应消息 type固定0x63
        return this;
    }
}
