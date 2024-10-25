package cn.paradox.service.impl;

import cn.paradox.domain.VO.WeixinTemplateMessageVO;
import cn.paradox.domain.Req.WeixinQrCodeReq;
import cn.paradox.domain.Res.WeiXinQRCodeRes;
import cn.paradox.domain.Res.WeixinTokenRes;
import cn.paradox.service.ILoginService;
import cn.paradox.service.weixin.IWeixinApiService;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeiXinLoginServiceImpl implements ILoginService {

    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String template_Id;

    @Resource
    private Cache<String, String> weixinAccessToken;

    @Resource
    private IWeixinApiService weixinApiService;

    @Resource
    private Cache<String, String> openidToken;


    @Override
    public String createQrCode() throws Exception {
        //1.获取accessToken
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if(null == accessToken){
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }

        //2.生成ticket
        //2.1 创建
        Call<WeiXinQRCodeRes> call = weixinApiService.createQrCode(accessToken, WeixinQrCodeReq.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeReq.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                .build())
                        .build())
                .build());
        WeiXinQRCodeRes weiXinQRCodeRes = call.execute().body();
        assert null != weiXinQRCodeRes;
        return weiXinQRCodeRes.getTicket();

    }

    @Override
    public String checkLogin(String ticket) {
        return openidToken.getIfPresent(ticket);
    }

    @Override
    public void saveLoginState(String ticket, String openid) throws Exception {
        openidToken.put(ticket, openid);

        //1. 获取accessToken 获取模板消息用
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if(null == accessToken){
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }

        //2，发送模板消息
        // 2. 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);

        WeixinTemplateMessageVO templateMessageDTO = new WeixinTemplateMessageVO(openid, template_Id);
        templateMessageDTO.setUrl("https://gaga.plus");
        templateMessageDTO.setData(data);

        Call<Void> call = weixinApiService.sendTemplateMessage(accessToken, templateMessageDTO);
        call.execute();
    }
}
