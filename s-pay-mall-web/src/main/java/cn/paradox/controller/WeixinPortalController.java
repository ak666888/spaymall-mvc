package cn.paradox.controller;

import cn.paradox.common.SignatureUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/portal/{appid}")
public class WeixinPortalController {
    private Logger logger = LoggerFactory.getLogger(WeixinPortalController.class);

    @Value("${weixin.config.originalid}")
    private String originalid;

    @Value("${weixin.config.token}")
    private String token;

    @GetMapping(value = "receive", produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr",required = false) String echostr
    ) {
        try {
            logger.info("微信公众号 {} 验签开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if(StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法");
            }
            boolean check = SignatureUtil.chech(token, signature, timestamp, nonce);
            logger.info("微信公众号 {} 验签完成 {}", appid, check);
            if(!check) {
                return null;
            }
            return echostr;
        }catch (Exception e){
            logger.info("微信公众号 {} 验签失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }

    }

}
