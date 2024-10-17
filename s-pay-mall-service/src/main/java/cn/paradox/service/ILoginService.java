package cn.paradox.service;

/**
 * 登录服务接口
 */
public interface ILoginService {
    /**
     * 生成登录二维码
     * @return
     * @throws Exception
     */
    String createQrCode() throws Exception;

    /**
     * 检查登录状态
     * @param ticket
     * @return
     */
    String checkLogin(String ticket);

    /**
     * 保存登录状态
     * @param ticket
     * @param openid
     * @throws Exception
     */
    void saveLoginState(String ticket, String openid) throws Exception;

}
