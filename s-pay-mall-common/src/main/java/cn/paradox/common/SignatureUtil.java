package cn.paradox.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 微信签名验证
 */
public class SignatureUtil {

    public static Logger logger = LoggerFactory.getLogger(SignatureUtil.class);

    public static  boolean chech(String token, String signature, String timestamp, String nonce) {
        logger.info("微信验签工具开始验签");
        //1.将token, timestamp, nonce 三个参数进行字典序排序
        String[] arr = new String[]{token, timestamp, nonce};
        sort(arr);
        //2.将排序后的三个参数拼接成一个字符串
        StringBuilder content = new StringBuilder();
        for(String s : arr){
            content.append(s);
        }
        //3.将字符串进行sha1加密
        MessageDigest md;
        String tempStr =  null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            tempStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //4.将加密后的字符串和signature对比，标识请求来源于微信
        logger.info("微信验签工具完成验签 {} 对比{}", tempStr, signature);
        return tempStr != null && tempStr.equalsIgnoreCase(signature);
    }

    /**\
     * 字符数组转换为16进制字符串
     * @param
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }

    /**
     * 将字节转换为十六进制字符串
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }

    /**
     * 进行字典排序
     */
    private static void sort(String[] str) {
        for (int i = 0; i < str.length - 1; i++) {
            for (int j = i + 1; j < str.length; j++) {
                if (str[j].compareTo(str[i]) < 0) {
                    String temp = str[i];
                    str[i] = str[j];
                    str[j] = temp;
                }
            }
        }
    }



}
