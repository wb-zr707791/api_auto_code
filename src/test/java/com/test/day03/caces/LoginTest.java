package com.test.day03.caces;

import com.test.day03.base.BaseCase;
import com.test.day03.POJO.CaseInfo;
import com.test.day03.data.Global;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * given 设置测试预设：包括请求头、请求参数、请求体、cookies等
 * when所要执行的操作 get/post
 * then解析结果、断言
 */
public class LoginTest extends BaseCase {
    List<CaseInfo> caseInfoList;


    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseDataFromExcel(1);
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getLoginDatas", priority = 2)
    public void testLogin(CaseInfo caseInfo) throws FileNotFoundException {
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        //jackson json 字符串--》map 注意：添加依赖
        //实现思路：如果是原始的字符串去转换会比较麻烦，，把原始的数据以json的格式去保存，再通过ObjectMapper再去转换去map
//        ObjectMapper mapper = new ObjectMapper();
//        //readValue 第一个参数：json字符串，第二个参数 转成的类型
//        Map mapHeaders = mapper.readValue(caseInfo.getRequestHeader(), Map.class);
        Map mapHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(),caseInfo.getCaseId());
        Response response =
                given().
                        headers(mapHeaders).
                        body(caseInfo.getInputParams()).
                when().
                        post(caseInfo.getUrl()).
                then().
                        log().all().extract().response();
        addLogToAllure(logFilePath);
        assertExpected(caseInfo, response);
        //登录模块用例执行结束之后，将memberID保存到变量当中
        //1、拿到正向用例返回响应信息里面的memberId
        if (caseInfo.getCaseId() == 1) {
            //2保存到环境变量中
            Global.env.put("token", response.path("data.token_info.token"));
        } else if (caseInfo.getCaseId() == 2) {
            Global.env.put("token1", response.path("data.token_info.token"));
        } else if (caseInfo.getCaseId() == 3) {
        }
        Global.env.put("token2", response.path("data.token_info.token"));
    }

    @DataProvider
    public Object[] getLoginDatas() {
        return caseInfoList.toArray();
    }
}
