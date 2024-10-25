package cn.paradox.controller;

import cn.paradox.common.weixin.SignatureUtil;
import cn.paradox.common.weixin.WXTextMessageEntity;
import cn.paradox.common.weixin.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/portal/")
public class WeixinPortalController {
    private Logger logger = LoggerFactory.getLogger(WeixinPortalController.class);

    @Value("${weixin.config.originalid}")
    private String originalid;

    @Value("${weixin.config.token}")
    private String token;

    /**
     * 验签接口
     * @param
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping(value = "receive", produces = "text/plain;charset=utf-8")
    public String validate(@RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr",required = false) String echostr
    ) {
        try {
            logger.info("微信公众号 {} 验签开始 [, {}, {}, {}]", signature, timestamp, nonce, echostr);
            if(StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法");
            }
            boolean check = SignatureUtil.chech(token, signature, timestamp, nonce);
            logger.info("微信公众号  验签完成 {}", check);
            if(!check) {
                return null;
            }
            return echostr;
        }catch (Exception e){
            logger.info("微信公众号 {} 验签失败 [, {}, {}, {}]", signature, timestamp, nonce, echostr, e);
            return null;
        }
    }


    /**
     * 接收微信消息接口
     */
    @PostMapping(value = "receive", produces = "application/xml; chatset=UTF-8")
    public String post(@RequestBody String requestBody){
       try{
           logger.info("接收微信公众号消息开始, {}",requestBody);
           //xml转bean对象
           WXTextMessageEntity textMessageEntity = XmlUtil.xmlToBean(requestBody, WXTextMessageEntity.class);

           WXTextMessageEntity res = new WXTextMessageEntity();
           res.setToUserName(textMessageEntity.getFromUserName());
           res.setFromUserName(textMessageEntity.getToUserName());
           res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
           res.setMsgType("text");
           res.setContent("急急急"+requestBody);
           String result = XmlUtil.beanToXml(res);
           logger.info("接收微信公众号消息成功  {}", result);
           return result;
       }catch (Exception e){
           logger.info("接收微信公众号消息失败 , {}",requestBody);
           return null;
       }

    }



}
