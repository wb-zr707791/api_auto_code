package com.test.day01;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestNgRestApi {
    @Test
    public void test() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "张三");
        map.put("age", "22");
        given().
                queryParams(map).
                when().
                get("http://httpbin.org/get").
                then().
                log().body();

    }

    @Test
    public void test2() {

        given().get("http://httpbin.org/get?name=张三&age=20").then().log().all();

        given().queryParam("name", "战三").queryParam("age", "21").
                get("http://httpbin.org/get").
                then().log().all();
    }

    @Test
    public void test3() {
        given().
                when().
                get("http://httpbin.org/get").
                then().
                log().body();
    }

    @Test
    public void test4() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "张三");
        map.put("age", "22");
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        given().contentType("application/x-www-form-urlencoded;charset=utf-8").
                formParams(map).
                when().
                post("http://httpbin.org/post").
                then().
                log().body();
    }

    @Test
    public void test5() {
       String jsonStr = "{\"mobile_phone\":\"13323231116\"," +
               "\"pwd\":\"12345678\"}";
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        given().contentType("application/json;charset=utf-8").
                body(jsonStr).
        when().
                post("http://httpbin.org/post").
        then().
                    log().body();
    }
    @Test
    public void test6() {
        String xml = "<?xml version = \"1.0\" encoding = \"utf-8\"?>" +
                "<suite><class>测试xml</class>" +
                "</suite>";
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        given().contentType(ContentType.XML).
                body(xml).
        when().
                post("http://httpbin.org/post").
        then().
                log().body();
    }

    @Test
    public void test7() {

        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        given().contentType("multipart/form-data;charset=utf-8").
                multiPart(new File("/Users/zhourui/Downloads/maven安装手册.docx")).
            when().
                post("http://httpbin.org/post").
                then().
                log().body();
    }
}
