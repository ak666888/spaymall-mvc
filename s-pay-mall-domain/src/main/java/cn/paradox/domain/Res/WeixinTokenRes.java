package cn.paradox.domain.Res;

import lombok.Data;

@Data
public class WeixinTokenRes {
    private String access_token;
    private int expires_in;
    private String errcode;
    private String errmsg;
}