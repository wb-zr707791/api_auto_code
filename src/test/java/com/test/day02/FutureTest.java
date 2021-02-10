package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day03.POJO.CaseInfo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
/**
 * given 设置测试预设：包括请求头、请求参数、请求体、cookies等
 * when所要执行的操作 get/post
 * then解析结果、断言
 */
public class FutureTest {
    List<CaseInfo> caseInfoList;

    @BeforeTest
    public void setUp(){
        caseInfoList=getCaseDataFromExcel(1);
    }

    private List<CaseInfo> getCaseDataFromExcel(int index) {
        File excelFile = new File("src/test/resources/前程贷1.3_接口测试用例完整版.xls");
        ImportParams params = new ImportParams();
        //开始读取的索引
        params.setStartSheetIndex(index);
        List<CaseInfo> list = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, params);
        return list;
    }

//    @Test
//    public void testRegister() {
//        String jsonStr = "{\"mobile_phone\":\"15336569798\"," +
//                "\"pwd\":\"12345678\"}";
//        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
//        given().
//                header("Content-Type", "application/json;charset=utf-8").
//                header("X-Lemonban-Media-Type", "lemonban.v1").
//                body(jsonStr).
//        when().
//                post("http://api.lemonban.com/futureloan/member/register").
//        then().
//                log().body();
//    }

    @Test(dataProvider = "getLoginDatas")
    public void testLogin(CaseInfo caseInfo) throws Exception {
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        //jackson json 字符串--》map 注意：添加依赖
        //实现思路：如果是原始的字符串去转换会比较麻烦，，把原始的数据以json的格式去保存，再通过ObjectMapper再去转换去map
        ObjectMapper mapper = new ObjectMapper();
        //readValue 第一个参数：json字符串，第二个参数 转成的类型
        Map mapHeaders = mapper.readValue(caseInfo.getRequestHeader(), Map.class);
        Response response =
        given().
                headers(mapHeaders).
                body(caseInfo.getInputParams()).
        when().
                post(caseInfo.getUrl()).
        then().
                log().all().extract().response();
        //ba 断言信息转换成map
        Map mapExpected = mapper.readValue(caseInfo.getExpected(), Map.class);
        //循环遍历、取到每一组键值对
        Set<Map.Entry<String, Object>> set = mapExpected.entrySet();
        for (Map.Entry<String, Object> map : set) {
            //我们在excel里面写用例的期望结果时。，期望姐过里面键名-->Gpath表单时
            Assert.assertEquals(response.path(map.getKey()), map.getValue());
        }
        //登录模块用例执行结束之后，将memberID保存到变量当中
        //1、拿到正向用例返回响应信息里面的memberId
        Integer memberId = response.path("data.id");
//        if (memberId!=null){
//            //2保存到环境变量中
//            Global.memberId = memberId;
//        }
    }
    @DataProvider
    public Object[] getLoginDatas() {
        return caseInfoList.toArray();
    }
//    @DataProvider
//    public Object[][] getLoginDatas(){
//        //1、请求接口地址、2、请求方式、3、请求头、4、请求数据
//        Object[][] datas = {
//                {"http://api.lemonban.com/futureloan/member/login","post","application/json;charset=utf-8","lemonban.v1","{\"mobile_phone\":\"13323232117\",\"pwd\":\"12345678\"}"},
//                {"http://api.lemonban.com/futureloan/member/login","post","application/json;charset=utf-8","lemonban.v1","{\"mobile_phone\":\"133232\",\"pwd\":\"12345678\"}"},
//                {"http://api.lemonban.com/futureloan/member/login","post","application/json;charset=utf-8","lemonban.v1","{\"mobile_phone\":\"13323232117\",\"pwd\":\"\"}"},
//                {"http://api.lemonban.com/futureloan/member/login","post","application/json;charset=utf-8","lemonban.v1","{\"mobile_phone\":\"\",\"pwd\":\"12345678\"}"}
//
//        };
//        return datas;
//    }


//    public static void main(String[] args) {
//        File excelFile = new File("src/test/resources/前程贷1.3_接口测试用例完整版.xls");
//        ImportParams params = new ImportParams();
//        //开始读取的索引
//        params.setStartSheetIndex(0);
//        //需要读几个
//        params.setSheetNum(2);
//        List<Object> objects = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, params);
//        for (Object object : objects) {
//            System.out.println(object);
//        }
//    }
}
