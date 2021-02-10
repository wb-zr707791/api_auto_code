package com.test.day03.POJO;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;


/**
 * 通过easy-poi映射的实体类，类中的属性需要和excel表头保持一直
 */
@Data
public class CaseInfo {

    @Excel(name = "序号")
    private int caseId;

    @Excel(name = "接口模块")
    private String interfaceName;

    @Excel(name = "用例标题")
    private String title;

    @Excel(name = "请求头")
    private String requestHeader;

    @Excel(name = "请求方式")
    private String method;

    @Excel(name = "接口地址")
    private String url;

    @Excel(name = "参数输入")
    private String inputParams;

    @Excel(name = "期望返回结果")
    private String expected;

    @Excel(name = "数据库结果")
    private String sqlResult;
}
