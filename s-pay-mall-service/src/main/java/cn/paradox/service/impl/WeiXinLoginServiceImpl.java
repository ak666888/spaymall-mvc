package cn.paradox.service.impl;

import cn.paradox.domain.Res.WeiXinQRCodeRes;
import cn.paradox.domain.Res.WeixinTokenRes;
import cn.paradox.service.ILoginService;
import cn.paradox.service.weixin.IWeixinApiService;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;

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
        WeiXinQRCodeRes weiXinQRCodeRes = weixinApiService.createQrCode(accessToken, )
        return "";
    }

    @Override
    public String checkLogin(String ticket) {
        return "";
    }

    @Override
    public void saveLoginState(String ticket, String openid) throws Exception {

    }
}
