package com.li.dto;

import com.li.pojo.RoomType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2024/1/4 13:53
 * @description
 */
@Data
public class RoomTypeDto extends RoomType {
    private BigDecimal price;
}
