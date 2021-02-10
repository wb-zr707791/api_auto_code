package com.test.day03.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day03.data.Constants;
import com.test.day03.data.Global;
import com.test.day03.POJO.CaseInfo;
import com.test.day03.util.JDBCUtils;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * 所有测试用例类的父类
 */
public class BaseCase {

    /**
     * 参数话替换
     *
     * @param caseInfoList
     * @return替换后的用例数据
     */
    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfoList) {
        //参数化处理（请求头、接口地址、参数输入、期望返回结果）
        for (CaseInfo caseInfo : caseInfoList) {

            String requestHeader = regexReplace(caseInfo.getRequestHeader());
            caseInfo.setRequestHeader(requestHeader);

            String url = regexReplace(caseInfo.getUrl());
            caseInfo.setUrl(url);

            String params = regexReplace(caseInfo.getInputParams());
            caseInfo.setInputParams(params);

            String expected = regexReplace(caseInfo.getExpected());
            caseInfo.setExpected(expected);

            String sqlResult = regexReplace(caseInfo.getSqlResult());
            caseInfo.setSqlResult(sqlResult);
        }
        return caseInfoList;
    }

    public CaseInfo paramsReplaceSingle(CaseInfo caseInfo) {
        //参数化处理（请求头、接口地址、参数输入、期望返回结果）

        String requestHeader = regexReplace(caseInfo.getRequestHeader());
        caseInfo.setRequestHeader(requestHeader);

        String url = regexReplace(caseInfo.getUrl());
        caseInfo.setUrl(url);

        String params = regexReplace(caseInfo.getInputParams());
        caseInfo.setInputParams(params);

        String expected = regexReplace(caseInfo.getExpected());
        caseInfo.setExpected(expected);

        String sqlResult = regexReplace(caseInfo.getSqlResult());
        caseInfo.setSqlResult(sqlResult);

        return caseInfo;
    }

    public String regexReplace(String sourceStr) {
        //如果参数化的源字符串为空，不需要进行参数化替换
        if (sourceStr == null) {
            return sourceStr;
        }
        //1、定义正则表达式
        String regex = "\\{(.*?)\\}";
        //2、通过正则表达式编译出来一个匹配器
        Pattern pattern = Pattern.compile(regex);
        //3、开始进行匹配,参数：是你要去在哪一个字符串去进行匹配
        Matcher matcher = pattern.matcher(sourceStr);
        //保存匹配到的整个表达式
        String findStr = "";
        //保存匹配到的小括号里面的内容 member_id
        String singleStr = "";
        //4、连续查找、匹配
        while (matcher.find()) {
            //输出找到匹配的结果，匹配到整个正则对应的字符串内容
            findStr = matcher.group(0);//{member_id}
//           System.out.println(matcher.group(1));
            singleStr = matcher.group(1);//member_id
            //5、先去找到环境变量里对应的值
            Object replaceStr = Global.env.get(singleStr);//member_id
            //6、替换原始字符串中的内容
            sourceStr = sourceStr.replace(findStr, replaceStr + "");
        }
        //没有匹配到，返回原样
        return sourceStr;
    }

    public List<CaseInfo> getCaseDataFromExcel(int index) {
        File excelFile = new File(Constants.EXCEL_PATH);
        ImportParams params = new ImportParams();
        //开始读取的索引
        params.setStartSheetIndex(index);
        List<CaseInfo> list = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, params);
        return list;
    }

    public void assertExpected(CaseInfo caseInfo, Response response) {
        try {
            ObjectMapper mapper2 = new ObjectMapper();
            Map mapExpected = mapper2.readValue(caseInfo.getExpected(), Map.class);
            Set<Map.Entry<String, Object>> set = mapExpected.entrySet();
            for (Map.Entry<String, Object> map : set) {
                //只需要转期望结果中的float类型或者是double类型的
                //map.getValue判断是不是float或者是double类型的
                if (map.getValue() instanceof Float || map.getValue() instanceof Double) {
                    BigDecimal bigDecimalValue = new BigDecimal(map.getValue().toString());
                    Assert.assertEquals(response.path(map.getKey()), bigDecimalValue);
                } else {
                    Assert.assertEquals(response.path(map.getKey()), map.getValue());
                }
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public Map fromJsonToMap(String jsonStr) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    @BeforeTest
    public void globalSetup() throws Exception {
        //设置接口响应结果如果是json返回的小数类型，使用BigDecimal类型来存储
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));

        //设置项目的日志存储到本地文件中
//        PrintStream fileOutPutStream = new PrintStream(new File("/Users/zhourui/Downloads/test_all"+System.currentTimeMillis()/1000+".log"));
//        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new ResponseLoggingFilter(fileOutPutStream));


    }

    public void assertSQL(CaseInfo caseInfo) {
        //数据库断言
        String checkSQL = caseInfo.getSqlResult();
        if (checkSQL != null) {
            Map checkSQLMap = fromJsonToMap(checkSQL);
            Set<Map.Entry<String, Object>> setCheckSQL = checkSQLMap.entrySet();
            for (Map.Entry<String, Object> mapEntry : setCheckSQL) {
                String sql = mapEntry.getKey();
                //查询数据库
                Object actual = JDBCUtils.querySingle(sql);
                if (actual instanceof Long) {
                    Long expectedValue = new Long(mapEntry.getValue().toString());
                    Assert.assertEquals(actual, expectedValue);
                } else if (actual instanceof BigDecimal) {
                    BigDecimal expected = new BigDecimal(mapEntry.getValue().toString());
                    Assert.assertEquals(actual, expected);
                } else {
                    Assert.assertEquals(actual, mapEntry.getValue());
                }
            }
        }
    }

    public String addLogToFile(String interfaceName, int caseId) {
        String logFilePath = "";
        //如果是debug 我需要在控制台打印日志方便查看
        //如果不是debug，则直接把日志输出到指定文件夹中
        if (!Constants.IS_DEBUG) {
            //创建指定目录的日志文件夹
            String dirPath = "target/test/log/" + interfaceName;
            File file = new File(dirPath);
            //如果目录不存在
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            logFilePath = dirPath + "/" + interfaceName + caseId + ".log";
            PrintStream printStream = null;
            try {
                //测试用例日志单独保存、请求之前对日志配置，输出到对应的文件中
                printStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));

        }
        return logFilePath;
    }

    public void addLogToAllure(String logFilePath) throws FileNotFoundException {
        if (!Constants.IS_DEBUG) {
            Allure.addAttachment("接口响应信息", new FileInputStream(logFilePath));
        }

    }
}