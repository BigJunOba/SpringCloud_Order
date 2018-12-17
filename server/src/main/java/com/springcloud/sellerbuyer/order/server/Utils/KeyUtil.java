package com.springcloud.sellerbuyer.order.server.Utils;

import java.util.Random;

public class KeyUtil {

    /**
    * @Description:生成唯一的主键，需要加同步关键字
    * @格式: 时间+随机数
    */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        // 生成六位随机数
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
