package com.springcloud.sellerbuyer.order.exception;

import com.springcloud.sellerbuyer.order.enums.ResultEnum;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException{

    private Integer code;

    public OrderException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public OrderException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
