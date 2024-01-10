package com.li.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author lzw
 * @Date 2024/1/2 17:32
 * @description 住宿情况管理
 */
@TableName("reservation")
@Data
public class Reservation implements Serializable {
    //住宿情况id
    @TableId("reservationId")
    private String reservationId;
    //房间id
    @TableField("roomId")
    private String roomId;
    //入住日期
    @TableField("checkInDate")
    private LocalDateTime checkInDate;
    //退房日期
    @TableField("checkOutDate")
    private LocalDateTime checkOutDate;
    //用户id
    @TableField("userId")
    private String userId;
    //用户预定所花的总的费用
    @TableField("totalCost")
    private BigDecimal totalCost;

}
