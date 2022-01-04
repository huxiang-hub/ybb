package org.springblade.saturn.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Data
public class ExcelErrorVo {
   private  List<Map<String, String>>  resultList;
   private Set<String> errorMessageList;

}
