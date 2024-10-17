package cn.paradox.controller;

import cn.paradox.common.Contants.Constants;
import cn.paradox.common.responce.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/wx/login/")
public class LoginController {




    @GetMapping(value = "weixin_qrcode_ticket")
    public Response<String> weixinQrcodeTicket() {
        try{
            String qrCodeTicket = loginService.createQrCodeTicket();
            log.info("生成微信登录二维码 ticket {}", qrCodeTicket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(qrCodeTicket).build();
        } catch (Exception e){
            log.error("生成微信登录二维码 ticket 失败", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
