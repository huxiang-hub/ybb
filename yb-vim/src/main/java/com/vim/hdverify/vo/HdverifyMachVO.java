package com.vim.hdverify.vo;

import com.vim.hdverify.entity.HdverifyMach;
import lombok.Data;

@Data
public class HdverifyMachVO extends HdverifyMach {

    // yb_machine_mixbox

    /**
     * 盒子的mac地址
     * */
    private String mac;

    // yb_sup***_boxinfo
    /**
     *盒子状态
     */
    private Integer boxStatus;
    /**
     * 盒子记录的数据
     */
    private Integer number;
    /**
     * 品牌
     */
    private String brand;
}
