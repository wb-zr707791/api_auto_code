package com.test.day03.caces;

import com.test.day03.base.BaseCase;
import com.test.day03.POJO.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;

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
public class GetUserInfoTest extends BaseCase {

    List<CaseInfo> caseInfoList;

    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseDataFromExcel(8);
        caseInfoList = paramsReplace(caseInfoList);
    }


    @Test(dataProvider = "getUserInfoDatas", priority = 3)
    public void testUserInfoGet(CaseInfo caseInfo) throws FileNotFoundException {
        //请求头由json专程map
        Map mapHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(),caseInfo.getCaseId());
        Response response =
                given().
                        headers(mapHeaders).
                when().
                        get(caseInfo.getUrl()).
                then().
                        log().all().extract().response();
        addLogToAllure(logFilePath);
        assertExpected(caseInfo, response);
    }

    @DataProvider
    public Object[] getUserInfoDatas() {
        return caseInfoList.toArray();
    }

}
