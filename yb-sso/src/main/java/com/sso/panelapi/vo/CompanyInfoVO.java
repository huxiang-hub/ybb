package com.sso.panelapi.vo;

import com.sso.panelapi.entity.CompanyInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 公司信息零时表
 *
 * @author Blade
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CompanyInfoVO", description = "虚拟表")
public class CompanyInfoVO extends CompanyInfo {

}
