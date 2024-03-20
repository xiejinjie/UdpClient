package com.github.xiejinjie.udpclient;


import com.github.xiejinjie.udpclient.common.CommonThreadPool;
import com.github.xiejinjie.udpclient.ui.AppUI;
import com.github.xiejinjie.udpclient.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * FileManagementApplication
 *
 * @author xiejinjie
 */
@SpringBootApplication
public class UdpClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(UdpClientApplication.class);

    public static void main(String[] args) {
        // 启动程序
        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(UdpClientApplication.class);
        appBuilder.web(WebApplicationType.NONE);
        appBuilder.bannerMode(Banner.Mode.LOG);
        appBuilder.headless(false);
        appBuilder.run(args);
        logger.info("(♥◠‿◠)ﾉﾞ  UdpClient 启动成功   ლ(´ڡ`ლ)ﾞ  " );
        logger.info("(♥◠‿◠)ﾉﾞ  WALL-E_III(瓦力三号)   ლ(´ڡ`ლ)ﾞ  " );
        logger.info("版本 " + SpringUtil.getProperty("app.version"));
        CommonThreadPool.threadPool.execute(() -> {
            AppUI.run(args);
        });
    }
}
