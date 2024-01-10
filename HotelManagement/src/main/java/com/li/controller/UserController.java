package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.li.common.R;
import com.li.common.UniqueIdGenerator;
import com.li.pojo.User;
import com.li.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.money.CurrencyUnitFormatter;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * @Author lzw
 * @Date 2024/1/3 14:11
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 添加用户
     */
    @PostMapping
    public R<String> save(@RequestBody User user){
        user.setUserId(UniqueIdGenerator.generateUniqueId());
        userService.save(user);
        return R.success("注册成功");
    }

    /**
     * 通过身份证号找到用户
     */
    @GetMapping("/getuser")
    public R<User> getUser(String idCardNo){
        System.out.println("身份证号："+idCardNo);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(idCardNo!=null,User::getIdCardNo,idCardNo);
        User user = userService.getOne(userLambdaQueryWrapper);
        return R.success(user);
    }

    /**
     * 通过身份证号删除用户
     * 在数据库中永久保存用户数据，删除只是把用户flag改成1，默认是0，查找用户只查找flag为1的用户
     */
    @DeleteMapping
    public R<String> deleteUserByIdCardNo(String idCardNo){
        User user = getUser(idCardNo).getData();
        if(user == null){
            return R.error("找不到该用户");
        }else{
            if(user.getFlag() == 0){
                user.setFlag(1);
                userService.updateById(user);
                return R.success("删除用户成功");
            }else{
                return R.error("该用户之前已被删除");
            }
        }
    }

    /**
     * 更新用户
     */
     @PostMapping("/update")
    public R<String> updateUser(@RequestBody User user){
         userService.updateById(user);
         return R.success("更新成功");
     }

    /**
     * 用户充值
     */
     @PostMapping("/recharge")
    public R<String> recharge(String idCardNo, String userName, BigDecimal count){
         User user = getUser(idCardNo).getData();
         if(user == null){
             return R.error("该用户不存在");
         }else if(!user.getUserName().equals(userName)){
             return R.error("请检查身份证号和用户名是否输入错误");
         }else{
             BigDecimal currentBalance = user.getBalance().add(count);
             user.setBalance(currentBalance);
             updateUser(user);
             return R.success("充值"+count+"元成功,"+"当前账户余额："+ currentBalance);
         }
     }

     @GetMapping("/list")
    public R<List<User>> list(@RequestParam(required = false) String userName, @RequestParam(required = false) String idCardNo){
         LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
         userLambdaQueryWrapper.eq(userName!=null,User::getUserName,userName);
         userLambdaQueryWrapper.eq(idCardNo!=null,User::getIdCardNo,idCardNo);
         List<User> list = userService.list(userLambdaQueryWrapper);
         return R.success(list);
     }



}
