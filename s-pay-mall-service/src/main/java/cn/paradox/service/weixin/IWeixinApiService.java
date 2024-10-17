package cn.paradox.service.weixin;

import cn.paradox.domain.PO.WeixinTemplateMessageVO;
import cn.paradox.domain.Req.WeixinQrCodeReq;
import cn.paradox.domain.Res.WeiXinQRCodeRes;
import cn.paradox.domain.Res.WeixinTokenRes;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IWeixinApiService {

    /**
     * 获取accessToken
     * @param grantType
     * @param appid
     * @param appSecret
     * @return
     */
    @GET("cgi-bin/token")
    Call<WeixinTokenRes> getToken(@Query("grant_type") String grantType,
                                  @Query("appid") String appid,
                                  @Query("secret") String appSecret);


    /**
     * 请求创造二维吗，获取ticker
     * @param accessToken
     * @param weixinQrCodeReq
     * @return
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeiXinQRCodeRes> createQrCode(@Query("access_token") String accessToken, @Body WeixinQrCodeReq weixinQrCodeReq);

    /**
     * 发送模板消息通知用户登录成功
     * @param accessToken
     * @param weixinTemplateMessageVO
     * @return
     */
    @POST("cgi-bin/message/template/send")
    Call<Void> sendTemplateMessage(@Query("access_token") String accessToken, @Body WeixinTemplateMessageVO weixinTemplateMessageVO);

}
