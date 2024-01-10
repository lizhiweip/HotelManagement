package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.common.R;
import com.li.common.UniqueIdGenerator;
import com.li.dto.RoomDto;
import com.li.pojo.Room;
import com.li.pojo.RoomPrice;
import com.li.pojo.RoomType;
import com.li.service.RoomPriceService;
import com.li.service.RoomService;
import com.li.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzw
 * @Date 2024/1/3 14:08
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private RoomPriceService roomPriceService;

    @PostMapping
    public R<Room> save(@RequestBody Room room, String roomTypeName){
        System.out.println("roomTypeName"+roomTypeName);
        LambdaQueryWrapper<RoomType> roomTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomTypeLambdaQueryWrapper.eq(roomTypeName != null,RoomType::getRoomTypeName, roomTypeName);
        RoomType roomType = roomTypeService.getOne(roomTypeLambdaQueryWrapper);
        System.out.println("roomTypeId111:" + roomType.getRoomTypeId());
        room.setRoomTypeId(roomType.getRoomTypeId());
        room.setRoomId(UniqueIdGenerator.generateUniqueId());
        log.info("新增房间："+ room);
        roomService.save(room);
        return R.success(room);
    }

    @DeleteMapping
    public R<String> deleteById(@RequestParam String roomId){
        Room room = roomService.getById(roomId);
        log.info("删除房间："+ room);
        roomService.removeById(roomId);
        return R.success("删除成功");
    }

    @PostMapping("/update")
    public R<Room> updateById(@RequestBody Room room){
        roomService.updateById(room);
        return R.success(room);
    }

    @GetMapping("/list")
    public R<List<RoomDto>> list(@RequestParam(required = false) String roomNumber,@RequestParam(required = false)Integer status, @RequestParam(required = false)String roomTypeName){
        LambdaQueryWrapper<Room> roomLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLambdaQueryWrapper.eq(roomNumber != null,Room::getRoomNumber, roomNumber);
        roomLambdaQueryWrapper.eq(status!= null,Room::getStatus,status);
        List<RoomDto> roomDtoList = new ArrayList<>();
        List<Room> roomList = roomService.list(roomLambdaQueryWrapper);
        for(Room room : roomList){
            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(room,roomDto);
            String roomTypeId = room.getRoomTypeId();
            LambdaQueryWrapper<RoomType> roomTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomTypeLambdaQueryWrapper.eq(roomTypeId!= null,RoomType::getRoomTypeId,roomTypeId);
            RoomType roomType = roomTypeService.getOne(roomTypeLambdaQueryWrapper);
            if(roomTypeName!=null && !roomTypeName.equals(roomType.getRoomTypeName()))continue;
            roomDto.setRoomTypeName(roomType.getRoomTypeName());
            LambdaQueryWrapper<RoomPrice> roomPriceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPriceLambdaQueryWrapper.eq(roomTypeId!= null, RoomPrice::getRoomTypeId,roomTypeId);
            RoomPrice roomPrice = roomPriceService.getOne(roomPriceLambdaQueryWrapper);
            roomDto.setPrice(roomPrice.getPrice());
            roomDtoList.add(roomDto);
        }
        return R.success(roomDtoList);
    }

    /**
     * 分页查询全部房间的所有信息，包括房间价格，房间类型，
     */
    @GetMapping("/page")
    public R<Page<RoomDto>> page(int page, int pageSize,
                        @RequestParam(required = false) BigDecimal minPrice,
                        @RequestParam(required = false) BigDecimal maxPrice,
                         @RequestParam(required = false) Integer status,
                         @RequestParam(required = false) String roomTypeName1){
        //构造分页构造器
        Page<Room> pageInfo = new Page<>(page,pageSize);
        Page<RoomDto> roomDtoPage = new Page<>();
        LambdaQueryWrapper<Room> roomLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLambdaQueryWrapper.eq(status!=null,Room::getStatus,status);
        //执行分页查询
        roomService.page(pageInfo,roomLambdaQueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,roomDtoPage,"records");
        List<Room> records = pageInfo.getRecords();

        List<RoomDto> list = records.stream().map((item) -> {
            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(item,roomDto);
            String roomTypeId = item.getRoomTypeId();//房间类型id
            //根据roomTypeId找到roomPrice对象
            LambdaQueryWrapper<RoomPrice> roomPriceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPriceLambdaQueryWrapper.eq(roomTypeId!= null,RoomPrice::getRoomTypeId,roomTypeId);
            RoomPrice roomPrice = roomPriceService.getOne(roomPriceLambdaQueryWrapper);
            BigDecimal priceValue = roomPrice.getPrice();
            if(roomPrice != null && (minPrice == null || priceValue.compareTo(minPrice)>=0) && (maxPrice == null || priceValue.compareTo(maxPrice)<=0)){
                  BigDecimal price = roomPrice.getPrice();
                  roomDto.setPrice(price);
            }else{
                return null;
            }
            //根据id查询roomType对象
            //如果roomTypeName1==null，那么就把所有的类型都找出来，如果不为空就模糊查询出所有符合的类型，如果在这个类型里面就放进去
            LambdaQueryWrapper<RoomType> roomTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomTypeLambdaQueryWrapper.like(roomTypeName1 != null,RoomType::getRoomTypeName,roomTypeName1);
            List<RoomType> list1 = roomTypeService.list(roomTypeLambdaQueryWrapper);
            log.info(list1.toString());

            RoomType roomType = roomTypeService.getById(roomTypeId);
            if(roomType != null){
                String roomTypeName= roomType.getRoomTypeName();
                System.out.println("当前的romtypename："+roomTypeName);
                if(roomTypeName1 == null || list1.stream().anyMatch(rt -> rt.getRoomTypeName().equals(roomTypeName))){
                    roomDto.setRoomTypeName(roomTypeName);
                }else{
                    return null;
                }
            }
            return roomDto;
        }).filter(roomDto -> roomDto != null).collect(Collectors.toList());
        roomDtoPage.setRecords(list);
        return R.success(roomDtoPage);
    }

    /**
     * 根据住宿情况表，如果到了住宿情况表中所在的时间，就把status改为1
     */




}
