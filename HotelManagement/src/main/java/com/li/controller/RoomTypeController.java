package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.li.common.R;
import com.li.common.UniqueIdGenerator;
import com.li.dto.RoomDto;
import com.li.dto.RoomTypeDto;
import com.li.pojo.Room;
import com.li.pojo.RoomPrice;
import com.li.pojo.RoomType;
import com.li.service.RoomPriceService;
import com.li.service.RoomService;
import com.li.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzw
 * @Date 2024/1/3 14:06
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/roomType")
public class RoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private RoomPriceService roomPriceService;
    @Autowired
    private RoomService roomService;

    /**
     * 新增房间类型
     */
    @PostMapping
    @Transactional
    public R<String> save(@RequestBody RoomType roomType){
     roomType.setRoomTypeId(UniqueIdGenerator.generateUniqueId());
     log.info("新增roomType:" + roomType);
     roomTypeService.save(roomType);
     String roomTypeId = roomType.getRoomTypeId();
     RoomPrice roomPrice = new RoomPrice();
     roomPrice.setPriceId(UniqueIdGenerator.generateUniqueId());
     roomPrice.setRoomTypeId(roomTypeId);
     roomPrice.setPrice(roomType.getBasePrice());
     //同时添加roomPrice
     roomPriceService.save(roomPrice);
     log.info("新增roomPrice:" + roomPrice);
     return R.success("添加房间类型成功");
    }

    /**
     *根据id删除roomType
     */
    @DeleteMapping
    @Transactional
    public R<String> deleteById(@RequestParam String roomTypeId){
          roomTypeService.removeById(roomTypeId);
          log.info("删除id为：" + roomTypeId + "的房间类型");
          LambdaQueryWrapper<RoomPrice> roomPriceLambdaQueryWrapper = new LambdaQueryWrapper<>();
          roomPriceLambdaQueryWrapper.eq(roomTypeId!=null,RoomPrice::getRoomTypeId,roomTypeId);
          //同时删除roomPrice
          roomPriceService.remove(roomPriceLambdaQueryWrapper);
          LambdaQueryWrapper<Room> roomLambdaQueryWrapper = new LambdaQueryWrapper<>();
          roomLambdaQueryWrapper.eq(roomTypeId!= null, Room::getRoomTypeId,roomTypeId);
          //同时删除room
          roomService.remove(roomLambdaQueryWrapper);
          return R.success("删除房间类型成功");
    }

    /**
     *根据id修改roomType
     */
    @Transactional
    @PostMapping("/update")
    public R<String> updateById(@RequestBody RoomTypeDto roomTypeDto){
        BigDecimal price = roomTypeDto.getPrice();
        String roomTypeId = roomTypeDto.getRoomTypeId();
        LambdaQueryWrapper<RoomPrice> roomPriceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomPriceLambdaQueryWrapper.eq(roomTypeId!=null, RoomPrice::getRoomTypeId,roomTypeId);
        RoomPrice roomPrice = roomPriceService.getOne(roomPriceLambdaQueryWrapper);
        roomPrice.setPrice(price);
        roomPriceService.updateById(roomPrice);
        RoomType roomType = new RoomType();
        BeanUtils.copyProperties(roomTypeDto,roomType);
        roomTypeService.updateById(roomType);
        return  R.success("修改成功");
    }

    /**
     * 查询全部房间类型
     */
    @GetMapping("/list")
    public R<List<RoomTypeDto>> list(){
        List<RoomType> roomTypeList = roomTypeService.list();
        List<RoomTypeDto> list = new ArrayList<>();
        for(RoomType roomType : roomTypeList){
            RoomTypeDto roomTypeDto = new RoomTypeDto();
            BeanUtils.copyProperties(roomType, roomTypeDto);
            String roomTypeId = roomType.getRoomTypeId();
            LambdaQueryWrapper<RoomPrice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(roomTypeId!=null,RoomPrice::getRoomTypeId,roomTypeId);
            RoomPrice roomPrice = roomPriceService.getOne(lambdaQueryWrapper);
            if (roomPrice != null) {
                BigDecimal price = roomPrice.getPrice();
                roomTypeDto.setPrice(price);
            }
            list.add(roomTypeDto);
        }
        return R.success(list);
    }

}
