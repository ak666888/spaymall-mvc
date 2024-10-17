package cn.paradox.domain.Res;

import lombok.Data;

@Data
public class WeiXinQRCodeRes {
    private String ticket;
    private Long expire_seconds;
    private String url;
}
