package com.li.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2024/1/2 17:27
 * @description
 */
@TableName("roomprice")
@Data
public class RoomPrice implements Serializable {
     @NotEmpty(message = "房间价格id不能为空")
     @TableId("priceId")
     private String priceId;
     @TableField("roomTypeId")
     private String roomTypeId;
     @TableField("price")
     private BigDecimal price;
}
