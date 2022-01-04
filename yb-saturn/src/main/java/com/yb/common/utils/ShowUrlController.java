package com.yb.common.utils;

import com.yb.common.OuterDataUrl;
import com.yb.execute.entity.ExecuteState;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by SUMMER
 * @date 2020/3/18.
 */
@RestController
@RequestMapping("/show")
public class ShowUrlController {

    @RequestMapping("/url")
    public void show(){
        ExecuteState executeState = new ExecuteState();
        OuterDataUrl.sendExestart(executeState);
    }
}
