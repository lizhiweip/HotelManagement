package com.li.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2024/1/7 17:04
 * @description
 */
@Data
@AllArgsConstructor
public class RevenueVo {
    private String roomNum;
    private BigDecimal totalAmount;
}
