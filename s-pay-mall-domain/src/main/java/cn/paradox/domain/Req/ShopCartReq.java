package cn.paradox.domain.Req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShopCartReq {

    private String userId;

    private String productId;
}
