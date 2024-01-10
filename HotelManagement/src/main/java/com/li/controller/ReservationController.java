package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.li.common.R;
import com.li.common.UniqueIdGenerator;
import com.li.dto.ReservationDto;
import com.li.pojo.*;
import com.li.service.ReservationService;
import com.li.service.RoomPriceService;
import com.li.service.RoomService;
import com.li.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lzw
 * @Date 2024/1/3 14:12
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    /**
     * 新增住宿情况,预定房间功能
     */
    @Transactional
    @PostMapping
    public R<String> save(String roomNum, @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime checkInDate,
                          @RequestParam("checkOutDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime checkOutDate, String idCardNo){
        //实现代码写在service层中，controller只调用该方法
          return reservationService.bookRoom(roomNum,checkInDate,checkOutDate,idCardNo);
    }

    /**
     * 住宿情况列表
     * @return
     */
    @Transactional
    @GetMapping("/list")
    public R<List<ReservationDto>> list(@RequestParam(required = false) String roomNum, @RequestParam(required = false) String userName){
        List<Reservation> reservationList = reservationService.list();
        List<ReservationDto> reservationDtoList = new ArrayList<>();
        for(Reservation reservation : reservationList){
            ReservationDto reservationDto = new ReservationDto();
            BeanUtils.copyProperties(reservation, reservationDto);
            //设置房间号
            String roomId = reservation.getRoomId();
            LambdaQueryWrapper<Room> roomLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLambdaQueryWrapper.eq(roomId!=null,Room::getRoomId,roomId);
            Room room = roomService.getOne(roomLambdaQueryWrapper);
            if(roomNum != null && !room.getRoomNumber().equals(roomNum)){
                continue;
            }else {
                reservationDto.setRoomNum(room.getRoomNumber());
            }
            //设置用户姓名
            String userId = reservation.getUserId();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(userId!=null,User::getUserId,userId);
            User user = userService.getOne(userLambdaQueryWrapper);
            if(userName != null && !user.getUserName().equals(userName)){
                continue;
            }else{
                reservationDto.setUserName(user.getUserName());
            }
            reservationDtoList.add(reservationDto);
        }
        return R.success(reservationDtoList);
    }




}
