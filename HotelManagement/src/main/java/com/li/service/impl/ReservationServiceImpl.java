package com.li.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.common.R;
import com.li.common.UniqueIdGenerator;
import com.li.mapper.ReservationMapper;
import com.li.pojo.*;
import com.li.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author lzw
 * @Date 2024/1/2 18:54
 * @description
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomPriceService roomPriceService;
    @Autowired
    private RevenueService revenueService;
    @Override
    @Transactional //线程同步每次只有一个线程可以执行这个方法
    public synchronized R<String> bookRoom(String roomNum, LocalDateTime checkInDate, LocalDateTime checkOutDate, String idCardNo) {
        //先检查时间是否合法，入住时间必须大于等于当前时间，退房时间必须大于入住时间
        LocalDateTime now = LocalDateTime.now();
        if (checkInDate.isBefore(now) || checkInDate.isAfter(checkOutDate)){
            return R.error("时间不合法");
        }
        //先查看这段时间内这个房间有没有被预定，如果再这段时间内已经有人预定了，就返回这段时间内已经有人预定
        LambdaQueryWrapper<Room> roomLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLambdaQueryWrapper.eq(roomNum!=null,Room::getRoomNumber,roomNum);
        Room room = roomService.getOne(roomLambdaQueryWrapper);
        String roomId = room.getRoomId();
        LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        reservationLambdaQueryWrapper.eq(roomId!= null,Reservation::getRoomId,roomId);
        List<Reservation> reservationList = reservationService.list(reservationLambdaQueryWrapper);
        for(Reservation reservation : reservationList){
            LocalDateTime checkInDate1 = reservation.getCheckInDate();
            if(checkInDate1.isAfter(checkInDate) && checkInDate1.isBefore(checkOutDate)){
                return R.error("这段时间内已经有用户预定");
            }
        }
        Reservation reservation = new Reservation();
        reservation.setReservationId(UniqueIdGenerator.generateUniqueId());
        //先通过用户的身份证号找到用户，如果不存在这个用户，提示不存在
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(idCardNo!=null,User::getIdCardNo,idCardNo);
        Integer flag = 0;
        userLambdaQueryWrapper.eq(User::getFlag, flag);
        User user = userService.getOne(userLambdaQueryWrapper);
        if(user == null){
            return R.error("请先添加该用户");
        }else{
            reservation.setUserId(user.getUserId());
            //先把时间转换成中国时间存进数据库
            ZoneId chinaTimeZone = ZoneId.of("Asia/Shanghai");
            checkInDate = checkInDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(chinaTimeZone).toLocalDateTime();
            checkOutDate = checkOutDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(chinaTimeZone).toLocalDateTime();
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            //通过roomNum得到roomType，然后通过roomType得到roomPrice,
            String roomTypeId = room.getRoomTypeId();
            LambdaQueryWrapper<RoomPrice> roomPriceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPriceLambdaQueryWrapper.eq(roomTypeId!= null, RoomPrice::getRoomTypeId,roomTypeId);
            RoomPrice roomPrice = roomPriceService.getOne(roomPriceLambdaQueryWrapper);
            BigDecimal price = roomPrice.getPrice();
            //计算天数
            if (checkInDate != null && checkOutDate != null) {
                Duration duration = Duration.between(checkInDate, checkOutDate);
                long days = duration.toDays();
                //价格乘以天数得到总价
                BigDecimal totalCost = price.multiply(BigDecimal.valueOf(days));
                reservation.setTotalCost(totalCost);
                reservation.setRoomId(room.getRoomId());
                //如果入住时间与当前时间只相差一天，把房间的当前状态改为1
                Duration duration1 = Duration.between(checkInDate,now);
                long hours = duration1.toHours();
                if(hours <= 24){
                    room.setStatus(1);
                    roomService.updateById(room);
                }
                reservationService.save(reservation);

                //从用户账户扣钱，新增一条营收记录
                if(user.getBalance().compareTo(totalCost) < 0){
                    return R.error("用户余额不足");
                }else{
                    user.setBalance(user.getBalance().subtract(totalCost));
                    userService.updateById(user);
                    //新增一条营收记录
                    Revenue revenue = new Revenue();
                    revenue.setRevenueId(UniqueIdGenerator.generateUniqueId());
                    revenue.setReservationId(reservation.getReservationId());
                    revenue.setDate(LocalDateTime.now());
                    revenue.setRoomNum(roomNum);
                    revenue.setAmount(totalCost);
                    revenueService.save(revenue);
                }
                return R.success("预定成功");
            }else{
                return R.error("出现问题请重试！");
            }

        }
    }
}
