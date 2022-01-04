package org.springblade.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Author my
 **/
@FeignClient(value = "yb-erpap", path = "/xunyue")
public interface XunYueClient {

    /**
     * 同步排产单（同时会同步工序及设备）
     *
     * @return
     */
    @GetMapping("/open")
    void open(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer shiftId);

    @GetMapping("/update")
    void update(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer status, @RequestParam Integer number);

    @GetMapping("/finishUpdate")
    void finishUpdate(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer number, @RequestParam Integer proNumber);

    @GetMapping("/updateBoxNum")
    void updateBoxNum(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer exId, @RequestParam Integer number,
                      @RequestParam String status, @RequestParam Integer wStatus, @RequestParam BigDecimal speed, @RequestParam Date beginTime);
}
