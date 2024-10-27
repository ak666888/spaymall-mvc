package cn.paradox.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "alipay", ignoreInvalidFields = true)
public class AliPayConfigProperties {
    /** 沙箱环境应用id */
    private String app_id;
    /** 沙箱环境 商户私钥  */
    private String merchant_private_key;
    /** 支付宝公钥 */
    private String alipay_public_key;
    /** 服务器异步通知页面路径 */
    private String notify_url;
    /** 页面跳转同步通知路径 */
    private String return_url;
    /**  */
    private String gatewayUrl;
    /** 签名方式 */
    private String sign_type = "RSA2";
    /**  字符编码格式 */
    private String charset = "utf-8";
    /**  传输格式 */
    private String format = "json";
}
