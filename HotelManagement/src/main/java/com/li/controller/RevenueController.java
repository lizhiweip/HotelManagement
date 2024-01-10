package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.li.common.R;
import com.li.dto.RevenueVo;
import com.li.pojo.Revenue;
import com.li.service.RevenueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lzw
 * @Date 2024/1/3 14:13
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/revenue")
public class RevenueController {
    @Autowired
    private RevenueService revenueService;
    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    @GetMapping("/list")
    public R<List<RevenueVo>> list(
            @RequestParam(required = false) String roomNum,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDateTime parsedStartDate = null;
        LocalDateTime parsedEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            parsedStartDate = LocalDateTime.parse(startDate, ISO_DATE_TIME_FORMATTER);
        }

        if (endDate != null && !endDate.isEmpty()) {
            parsedEndDate = LocalDateTime.parse(endDate, ISO_DATE_TIME_FORMATTER);
        }


        LambdaQueryWrapper<Revenue> revenueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        revenueLambdaQueryWrapper.eq(roomNum != null, Revenue::getRoomNum, roomNum);
        if (parsedStartDate != null && parsedEndDate != null) {
            revenueLambdaQueryWrapper.between(Revenue::getDate, parsedStartDate, parsedEndDate);
        } else if (parsedStartDate != null) {
            revenueLambdaQueryWrapper.ge(Revenue::getDate, parsedStartDate);
        } else if (parsedEndDate != null) {
            revenueLambdaQueryWrapper.le(Revenue::getDate, parsedEndDate);
        }

        List<Revenue> list = revenueService.list(revenueLambdaQueryWrapper);

        Map<String, BigDecimal> totalAmountByRoomNum = list.stream()
                .collect(Collectors.groupingBy(Revenue::getRoomNum,
                        Collectors.mapping(Revenue::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        List<RevenueVo> result = totalAmountByRoomNum.entrySet().stream()
                .map(entry -> new RevenueVo(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(RevenueVo::getTotalAmount).reversed())
                .collect(Collectors.toList());
        return   R.success(result);
    }

    @GetMapping("/sort")
    public R<List<RevenueVo>> sort(
            @RequestParam(required = false) String roomNum,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Boolean asc, @RequestParam(required = false)Integer num) {

        LocalDateTime parsedStartDate = null;
        LocalDateTime parsedEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            parsedStartDate = LocalDateTime.parse(startDate, ISO_DATE_TIME_FORMATTER);
        }

        if (endDate != null && !endDate.isEmpty()) {
            parsedEndDate = LocalDateTime.parse(endDate, ISO_DATE_TIME_FORMATTER);
        }

        LambdaQueryWrapper<Revenue> revenueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        revenueLambdaQueryWrapper.eq(roomNum != null, Revenue::getRoomNum, roomNum);
        if (parsedStartDate != null && parsedEndDate != null) {
            revenueLambdaQueryWrapper.between(Revenue::getDate, parsedStartDate, parsedEndDate);
        } else if (parsedStartDate != null) {
            revenueLambdaQueryWrapper.ge(Revenue::getDate, parsedStartDate);
        } else if (parsedEndDate != null) {
            revenueLambdaQueryWrapper.le(Revenue::getDate, parsedEndDate);
        }

        List<Revenue> list = revenueService.list(revenueLambdaQueryWrapper);

        Map<String, BigDecimal> totalAmountByRoomNum = list.stream()
                .collect(Collectors.groupingBy(Revenue::getRoomNum,
                        Collectors.mapping(Revenue::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
        List<RevenueVo> result ;
        if(asc == null || !asc){
            System.out.println(asc);
             result = totalAmountByRoomNum.entrySet().stream()
                    .map(entry -> new RevenueVo(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(RevenueVo::getTotalAmount).reversed())
                    .collect(Collectors.toList());
        }else{
            System.out.println("111111"+asc);
            result = totalAmountByRoomNum.entrySet().stream()
                    .map(entry -> new RevenueVo(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(RevenueVo::getTotalAmount))
                    .collect(Collectors.toList());
        }
        if(num != null){
            List<RevenueVo> result2 = new ArrayList<>();
            int len = num < result.size() ? num : result.size();
            for(int i=0;i<len;i++){
                result2.add(result.get(i));
            }
            return R.success(result2);
        }
        return   R.success(result);
    }

}
