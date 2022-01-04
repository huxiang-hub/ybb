package com.yb.statis.service;

import com.yb.statis.request.DeviceCapacityProgressRequest;
import com.yb.statis.request.DeviceCurrentOrderRequest;
import com.yb.statis.vo.DeviceCapacityProgressListVO;
import com.yb.statis.vo.DeviceCapacityProgressVO;
import com.yb.statis.vo.DeviceCurrentOrderVO;
import com.yb.statis.vo.DeviceOrderNumProgressVO;

import java.text.ParseException;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/22 19:56
 */
public interface StatisCapacityService {

   List<DeviceCurrentOrderVO> deviceCurrentOrder(DeviceCurrentOrderRequest request);

   DeviceCapacityProgressVO deviceCapacityProgress (DeviceCapacityProgressRequest request) throws ParseException;

   DeviceOrderNumProgressVO deviceOrderNumProgress(DeviceCapacityProgressRequest request) throws ParseException;




}
