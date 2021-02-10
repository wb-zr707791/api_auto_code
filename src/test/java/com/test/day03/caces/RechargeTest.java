package com.test.day03.caces;

import com.test.day03.base.BaseCase;
import com.test.day03.POJO.CaseInfo;
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

public class RechargeTest extends BaseCase {
    public class GetUserInfoTest extends BaseCase {

        List<CaseInfo> caseInfoList;

        @BeforeClass
        public void setUp() {
            caseInfoList = getCaseDataFromExcel(2);
            caseInfoList = paramsReplace(caseInfoList);
        }

        @Test(dataProvider = "getRechargeDatas", priority = 4)
        public void testRecharge(CaseInfo caseInfo) throws FileNotFoundException {
            //请求头由json专程map
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
            assertExpected(caseInfo, response);
            addLogToAllure(logFilePath);
            if (response.path("data.mobile_phone") != null) {
                assertSQL(caseInfo);
            }
        }


        @DataProvider
        public Object[] getRechargeDatas() {
            return caseInfoList.toArray();
        }

    }
}
