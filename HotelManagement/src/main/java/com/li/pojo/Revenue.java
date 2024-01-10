package com.li.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author lzw
 * @Date 2024/1/2 17:38
 * @description 营收情况：一条记录表示某一天某个房间产生的收入
 */
@TableName("revenue")
@Data
public class Revenue implements Serializable {
    //营收记录id，

    @TableId("revenueId")
    private String revenueId;
    //房间id
    @TableField("roomNum")
    private String roomNum;
    //住宿情况记录的id，
    //根据住宿情况中的一条记录来得出某个房间在这一天的收入
    @TableField("reservationId")
    private String reservationId;
    //金额
    @TableField("amount")
    private BigDecimal amount;
    //日期
    @TableField("date")
    private LocalDateTime date;

}
