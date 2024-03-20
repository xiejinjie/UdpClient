package com.github.xiejinjie.udpclient.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AppUI
 *
 * @author xiejinjie
 * @date 2023/12/25
 */
public class AppUI extends Application {
    private static final Logger logger = LoggerFactory.getLogger(AppUI.class);


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UdpClientView.fxml"));
            // 创建布局
            Scene scene = new Scene(loader.load() , 500, 480);
            primaryStage.setScene(scene);
            primaryStage.setTitle("UdpClient");
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("fms.png"));
            // 设置controller
            UdpClientController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.show();
        } catch (Throwable e) {
            logger.error("UI启动失败", e);
            System.exit(1);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void run(String[] args) {
        launch();
    }
}
