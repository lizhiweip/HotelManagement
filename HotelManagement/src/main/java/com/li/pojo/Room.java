package com.li.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lzw
 * @Date 2024/1/2 17:20
 * @description
 */
@TableName("room")
@Data
public class Room implements Serializable {
    @TableId("roomId")
    private String roomId;
    @NotEmpty(message = "房间号不能为空")//添加房间时提醒
    @TableField("roomNumber")
    private String roomNumber;

    //房间状态，0表示未预定，1表示已预定，-1表示维护中
    @TableField("status")
    private Integer status;
    @TableField("description")
    private String description;
    @TableField("roomTypeId")
    private String roomTypeId;
}
