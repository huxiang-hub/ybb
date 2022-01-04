package com.vim.chatapi.statistics;

import com.vim.user.service.IImUserService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by SUMMER
 * @date 2020/4/6.
 */
@RestController
@RequestMapping("/statist")
public class StatisticsController {


    @Autowired
    private IImUserService userService;


    /**
     * 统计用户的生产良品和废品数量
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getUserStatist")
    public R getStatist(String userId, String startDate, String endDate) {
        return R.data(userService.getStatist(userId, startDate, endDate));
    }
}
