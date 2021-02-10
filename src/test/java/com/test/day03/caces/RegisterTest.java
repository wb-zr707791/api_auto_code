package com.test.day03.caces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day03.base.BaseCase;
import com.test.day03.data.Constants;
import com.test.day03.data.Global;
import com.test.day03.POJO.CaseInfo;
import com.test.day03.util.PhoneRandom;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;

import java.util.List;
import java.util.Map;

@Slf4j
public class RegisterTest extends BaseCase {
    List<CaseInfo> caseInfoList;

    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseDataFromExcel(0);
    }

    @Test(dataProvider = "getRegisterDatas", priority = 1)
    public void register(CaseInfo caseInfo) throws Exception {

        if (caseInfo.getCaseId() == 1) {
            String phone = PhoneRandom.getPhone();
            Global.env.put("mobile_phone", phone);
            log.info("{}.测试打印用例一中的手机号码日志", phone);
        } else if (caseInfo.getCaseId() == 2) {
            String phone1 = PhoneRandom.getPhone();
            Global.env.put("mobile_phone1", phone1);
        } else if (caseInfo.getCaseId() == 3) {
            String phone2 = PhoneRandom.getPhone();
            Global.env.put("mobile_phone2", phone2);
        }
        //参数化替换 --对当前的case todo "需要再看一下这个逻辑"
        caseInfo = paramsReplaceSingle(caseInfo);
        //RestAssured.baseURI,可以通过这个设置通用的baseURI，并且只需要配置一次
        Map mapHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        //将该模块下的用例日志输出到指定的文件夹中,并用一个变量接收返回值，保存下文件路径
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(),caseInfo.getCaseId());
        Response response =
        given().
                headers(mapHeaders).
                body(caseInfo.getInputParams()).
        when().
                post(caseInfo.getUrl()).
        then().
                log().all().extract().response();
        //将请求和响应信息添加到allure中，放在断言之前添加，否则断言失败了有可能就不添加了
        //第一个参数是附件的名字、第二个参数是
        addLogToAllure(logFilePath);
        //响应结果断言
        assertExpected(caseInfo, response);
        //数据库断言
        assertSQL(caseInfo);
        //注册成功的密码从用例数据中拿取
        String inputParams = caseInfo.getInputParams();
        ObjectMapper mapperParam = new ObjectMapper();
        Map mapPassword = mapperParam.readValue(inputParams, Map.class);

        if (caseInfo.getCaseId() == 1) {
            Object mobilePhone = response.path("data.mobile_phone");
            if (mobilePhone != null) {
                Global.env.put("mobile_phone", mobilePhone);
            }
            Global.env.put("pwd", mapPassword.get("pwd") + "");
            Global.env.put("member_id", response.path("data.id"));

        } else if (caseInfo.getCaseId() == 2) {
            Object mobilePhone = response.path("data.mobile_phone");
            if (mobilePhone != null) {
                Global.env.put("mobile_phone1", mobilePhone);
            }
            Global.env.put("pwd1", mapPassword.get("pwd") + "");
            Global.env.put("member_id1", response.path("data.id"));

        } else if (caseInfo.getCaseId() == 3) {
            Object mobilePhone = response.path("data.mobile_phone");
            if (mobilePhone != null) {
                Global.env.put("mobile_phone2", mobilePhone);
            }
            Global.env.put("pwd2", mapPassword.get("pwd") + "");
            Global.env.put("member_id2", response.path("data.id"));

        }
    }

    @DataProvider
    public Object[] getRegisterDatas() {
        return caseInfoList.toArray();
    }

}


