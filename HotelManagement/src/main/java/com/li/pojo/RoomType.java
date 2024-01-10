package com.li.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2024/1/2 17:17
 * @description
 */
@TableName("roomType")
@Data
public class RoomType {
    @NotEmpty(message = "房间类型id不能为空")//在添加房间类型时提醒
    @TableId("roomTypeId")
    private String roomTypeId;
    @NotEmpty(message = "房间类型名称不能为空")
    @TableField("roomTypeName")
    private String roomTypeName;
    @TableField("description")
    private String description;
    @TableField("basePrice")
    private BigDecimal basePrice;
}
