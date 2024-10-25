package cn.paradox.domain.Req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeixinQrCodeReq {
    private int expire_seconds;
    private String action_name;
    private ActionInfo action_info;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ActionInfo {
        Scene scene;

        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @Data
        public static class Scene {
            int scene_id;
            String scene_str;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ActionNameTypeVO {
        QR_SCENE("QR_SCENE", "临时的整型参数值"),
        QR_STR_SCENE("QR_STR_SCENE", "临时的字符串参数值"),
        QR_LIMIT_SCENE("QR_LIMIT_SCENE", "永久的整型参数值"),
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE", "永久的字符串参数值");

        private String code;
        private String info;
    }


}
