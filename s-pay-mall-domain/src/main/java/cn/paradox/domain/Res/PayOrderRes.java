package cn.paradox.domain.Res;

import cn.paradox.common.Contants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付单返回
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderRes {
    private String userId;
    private String orderId;
    private String payUrl;
    private Constants.OrderStatusEnum orderStatusEnum;
}
