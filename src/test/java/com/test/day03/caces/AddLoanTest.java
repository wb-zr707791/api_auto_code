package com.test.day03.caces;

import com.test.day03.base.BaseCase;
import com.test.day03.data.Global;
import com.test.day03.POJO.CaseInfo;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AddLoanTest extends BaseCase {
    private static Logger logger = Logger.getLogger(AddLoanTest.class);
    List<CaseInfo> caseInfoList;

    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseDataFromExcel(3);
        caseInfoList = paramsReplace(caseInfoList);
    }


    @Test(dataProvider = "getAddLoanDatas", priority = 5)
    public void testAddLoan(CaseInfo caseInfo) throws FileNotFoundException {
        //请求头由json专程map
        Map mapHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(),caseInfo.getCaseId());
        Response response =
                given().log().all().//在这里添加log.all可以实现打印日志信息的效果
                        headers(mapHeaders).
                        body(caseInfo.getInputParams()).
                when().
                        post(caseInfo.getUrl()).
                then().
                        log().all().extract().response();
        addLogToAllure(logFilePath);
        assertExpected(caseInfo, response);
        if (response.path("data.id") != null) {
            Global.env.put("loan_id", response.path("data.id"));
            logger.info("向环境变量中添加loan_id成功");
        }
    }


    @DataProvider
    public Object[] getAddLoanDatas() {
        return caseInfoList.toArray();
    }
}
