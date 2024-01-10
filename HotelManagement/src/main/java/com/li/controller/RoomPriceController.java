package com.li.controller;

import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.li.common.R;
import com.li.pojo.Room;
import com.li.pojo.RoomPrice;
import com.li.service.RoomPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author lzw
 * @Date 2024/1/3 14:09
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/roomPrice")
public class RoomPriceController {
    @Autowired
    private RoomPriceService roomPriceService;

    /**
     * 查找所有的房间类型的价格
     * @return
     */
    @GetMapping("/list")
    public R<List<RoomPrice>> list(){
        List<RoomPrice> list = roomPriceService.list();
        return R.success(list);
    }

    @PostMapping("/update")
    public R<String> update(@RequestBody RoomPrice roomPrice){
        roomPriceService.updateById(roomPrice);
        return R.success("修改房间价格成功");
    }

    /**
     * 所有房间打折
     */
     @PutMapping("/discount")
     public R<String> discount(@RequestParam BigDecimal discountPercentage) {
         try {
             List<RoomPrice> roomPrices = roomPriceService.list();
             roomPrices.forEach(roomPrice -> {
                 if (roomPrice != null) {
                     BigDecimal currentPrice = roomPrice.getPrice();
                     BigDecimal discountedPrice = currentPrice.multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
                     roomPrice.setPrice(discountedPrice);
                     roomPriceService.updateById(roomPrice);
                 }
             });
             return R.success("所有房价价格已经加上折扣！");
         } catch (Exception e) {
             return R.error("出现了一些问题：" + e.getMessage());
         }
     }

    /**
     * 所有房间降价或加价
     */

    @PutMapping("/decrease")
    public R<String> decrease(@RequestParam BigDecimal de){
        try {
            List<RoomPrice> roomPrices = roomPriceService.list();
            roomPrices.forEach(roomPrice -> {
                if (roomPrice != null) {
                    BigDecimal currentPrice = roomPrice.getPrice();
                    BigDecimal discountedPrice = currentPrice.subtract(de.multiply(BigDecimal.valueOf(-1)));
                    roomPrice.setPrice(discountedPrice);
                    roomPriceService.updateById(roomPrice);
                }
            });
            return R.success("所有房价价格已经增加" + de + "元");
        } catch (Exception e) {
            return R.error("出现了一些问题：" + e.getMessage());
        }
    }

}
