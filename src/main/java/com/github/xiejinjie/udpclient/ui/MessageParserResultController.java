package com.github.xiejinjie.udpclient.ui;

import com.github.xiejinjie.udpclient.message.Message;
import com.github.xiejinjie.udpclient.message.MessageResp;
import com.github.xiejinjie.udpclient.message.MessageTlv;
import com.github.xiejinjie.udpclient.message.MessageUtil;
import com.github.xiejinjie.udpclient.util.SocketUtil;
import com.github.xiejinjie.udpclient.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * SendOfficeSelectorController
 *
 * @author xiejinjie
 * @date 2024/2/2
 */
public class MessageParserResultController {
    private static final Logger logger = LoggerFactory.getLogger(MessageParserResultController.class);

    @FXML public TextArea message;
    @FXML public TextArea parseMessageResult;
    private Stage stage; // 用于引用当前模态窗口的Stage


    // 设置当前模态窗口的Stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParseMessageResult(String info) {
        parseMessageResult.setText(info);
    }

    @FXML
    private void parseMsg() {
        String msg = message.getText();
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        try {
            msg = msg.trim();
            Message message = MessageUtil.parseMessage(SocketUtil.hexStringToBytes(msg));
            StringBuilder info = new StringBuilder();
            info.append("type=").append(message.getType()).append("\n");
            info.append("requestId=").append(message.getRequestId()).append("\n");
            if (message instanceof MessageResp) {
                info.append("status=").append(((MessageResp)message).getStatus()).append("\n");
            }
            info.append("len=").append(message.getLen()).append("\n");
            List<MessageTlv> tlvList = message.getTlvList();
            if (StringUtils.isNotEmpty(tlvList)) {
                for (int i = 0; i < tlvList.size(); i++) {
                    MessageTlv tlv = tlvList.get(i);
                    info.append("tlv").append(i + 1)
                            .append("[")
                            .append("tag=").append(tlv.getTag()).append(",")
                            .append("len=").append(tlv.getLength()).append(",")
                            .append("value=").append(SocketUtil.bytesToHexString(tlv.getValue()))
                            .append("]").append("\n");
                }
            }
            parseMessageResult.setText(info.toString());
        } catch (Exception e) {
            parseMessageResult.setText("");
            logger.error("解析失败", e);
        }
    }

    @FXML
    private void closeDialog() {
        if (stage != null) {
            stage.close(); // 关闭对话框
        }
    }
}
