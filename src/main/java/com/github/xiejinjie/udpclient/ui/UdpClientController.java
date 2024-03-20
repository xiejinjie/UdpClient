package com.github.xiejinjie.udpclient.ui;

import com.github.xiejinjie.udpclient.business.service.IUdpClientService;
import com.github.xiejinjie.udpclient.common.CommonThreadPool;
import com.github.xiejinjie.udpclient.util.SocketUtil;
import com.github.xiejinjie.udpclient.util.SpringUtil;
import com.github.xiejinjie.udpclient.util.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * ReceiveFileController
 *
 * @author xiejinjie
 * @date 2023/12/26
 */
public class UdpClientController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(UdpClientController.class);
    @FXML public TextField hostField;
    @FXML public TextField portField;
    @FXML private TextArea sendText;
    @FXML private TextArea receiveText;
    @FXML private RadioButton hasReturn;
    @FXML private RadioButton hasNoReturn;

    private ToggleGroup hasReturnGroup;

    private Stage primaryStage;

    private IUdpClientService udpClientService;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.udpClientService = SpringUtil.getBean(IUdpClientService.class);
        hasReturnGroup = new ToggleGroup();
        this.hasReturn.setToggleGroup(hasReturnGroup);
        this.hasNoReturn.setToggleGroup(hasReturnGroup);
        this.hasReturn.setSelected(true);
    }

    @FXML
    private void sendUdpPacket() {
        CommonThreadPool.threadPool.execute(() -> {
            if (!checkParam()) {
                logger.warn("参数不合法");
                return;
            }
            SendInfo sendInfo = new SendInfo();
            sendInfo.setHost(hostField.getText());
            sendInfo.setPort(Integer.valueOf(portField.getText()));
            sendInfo.setSendBytes(SocketUtil.hexStringToBytes(sendText.getText()));
            sendInfo.setHasReturn(hasReturn.isSelected());
            receiveText.clear();
            logger.info("发送Udp数据, {}", sendInfo);
            CompletableFuture<byte[]> futureBytes = udpClientService.sendUdpPacket(sendInfo);
            showSuccessAlert("发送成功");
            if (sendInfo.getHasReturn() && futureBytes != null) {
                try {
                    byte[] bytes = futureBytes.get();
                    receiveText.setText(SocketUtil.bytesToHexString(bytes));
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("获取响应消息失败");
                }
            }
        });
    }

    private boolean checkParam() {
        if (StringUtils.isEmpty(hostField.getText())) {
            logger.error("发送地址为空");
            return false;
        }
        String port = portField.getText();
        if (StringUtils.isEmpty(port) && port.matches("\\d+")) {
            logger.error("端口不合法");
            return false;
        }
        if (StringUtils.isEmpty(sendText.getText())) {
            logger.error("发送内容为空");
            return false;
        }
        return true;
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
