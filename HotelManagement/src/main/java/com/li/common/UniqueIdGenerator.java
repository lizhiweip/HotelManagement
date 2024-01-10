package com.li.common;

import java.time.Instant;
import java.util.UUID;

/**
 * @Author lzw
 * @Date 2024/1/3 14:29
 * @description
 */
public class UniqueIdGenerator {
    public static String generateUniqueId() {
        // 获取当前时间戳
        long timestamp = Instant.now().toEpochMilli();

        // 生成随机UUID
        String uuid = UUID.randomUUID().toString();

        // 去掉UUID中的"-"
        uuid = uuid.replaceAll("-", "");

        // 取UUID的前8位
        String uuidPrefix = uuid.substring(0, 8);

        // 拼接时间戳和UUID前8位
        return timestamp + uuidPrefix;
    }

}
