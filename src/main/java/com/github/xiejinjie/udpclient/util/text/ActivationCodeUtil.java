package com.github.xiejinjie.udpclient.util.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * VerificationCodeUtil
 *
 * @author xiejinjie
 * @date 2023/12/20
 */
public class ActivationCodeUtil {
    private static Logger logger = LoggerFactory.getLogger(ActivationCodeUtil.class);

    public static String generateCode(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb=new StringBuilder();
        for(int i = 0; i<length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        String code = sb.toString();
        logger.info("激活校验码:" + code);
        return code;
    }
}
