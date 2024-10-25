package cn.paradox.controller;

import cn.paradox.common.Contants.Constants;
import cn.paradox.common.responce.Response;
import cn.paradox.service.ILoginService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/wx/login/")
public class LoginController {

    @Resource
    private ILoginService loginService;


    @GetMapping(value = "weixin_qrcode_ticket")
    public Response<String> weixinQrcodeTicket() {
        try{
            String qrCodeTicket = loginService.createQrCode();
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

    @GetMapping(value = "check_login")
    public Response<String> checkLogin(@RequestParam String ticket) {
        try{
            String openidToken = loginService.checkLogin(ticket);
            log.info("微信登录校验 ticket{}", ticket);
            if (StringUtils.isNotBlank(openidToken)) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .info(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(openidToken)
                        .build();
            } else {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.NO_LOGIN.getCode())
                        .info(Constants.ResponseCode.NO_LOGIN.getInfo())
                        .build();
            }

        }catch (Exception e){
            log.info("微信登录校验失败 ticket{}", ticket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
