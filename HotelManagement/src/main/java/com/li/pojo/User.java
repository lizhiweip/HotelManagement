package com.li.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2024/1/2 17:30
 * @description
 */
@TableName("user")
@Data
public class User implements Serializable {
    @TableId("userId")
    private String userId;
    @TableField("userName")
    private String userName;

    //身份证号
    @TableField("idCardNo")
    private String idCardNo;
    @TableField("description")
    private String description;
    @TableField("phoneNum")
    private String phoneNum;
    @TableField("flag")
    private Integer flag;

    @TableField("balance")
    private BigDecimal balance;
}
