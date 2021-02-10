package com.test.day01;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class RestApiTest {
    public static void main(String[] args) {
        /**
         * given 设置测试预设：包括请求头、请求参数、请求体、cookies等
         * when所要执行的操作 get/post
         * then解析结果、断言
         */
//        given().
//        when().
//                get("http://httpbin.org/get").
//        then().
//                log().body();
//        given().get("http://httpbin.org/get?name=张三&age=20").then().log().all();

//        given().queryParam("name", "战三").queryParam("age", "21").
//                get("http://httpbin.org/get").
//                then().log().all();
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
}
