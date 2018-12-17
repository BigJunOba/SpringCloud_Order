package com.springcloud.sellerbuyer.order.server.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    CART_EMPTY(2, "购物车为空"),

    ORDER_OWNER_ERROR(19, "该订单不属于当前用户"),

    WX_MP_ERROR(20, "微信公众账号方面错误"),

    WXPAY_NOTIFY_MONRY_VERIFY_ERROR(21, "微信支付异步通知金额校验不通过"),

    ORDER_CANCEL_SUCCESS(22, "订单取消成功！"),

    ORDER_FINISH_SUCCESS(23, "订单完结成功！"),

    PRODUCT_STATUS_ERROR(24, "商品状态不正确"),

    LOGIN_FAIL(25, "登录失败，登录信息不正确"),

    LOGOUT_SUCCESS(26, "登出成功")
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
