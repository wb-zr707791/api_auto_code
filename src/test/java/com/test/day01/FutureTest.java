package com.test.day01;

import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class FutureTest {
    @Test
    public void testRegister() {
        String jsonStr = "{\"mobile_phone\":\"13323232117\"," +
                "\"pwd\":\"12345678\"}";
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        given().
                header("Content-Type","application/json;charset=utf-8").
                header("X-Lemonban-Media-Type","lemonban.v1").
                body(jsonStr).
        when().
                post("http://api.lemonban.com/futureloan/member/register").
        then().
                log().body();
    }
    @Test
    public void testLogin() {
        String jsonStr = "{\"mobile_phone\":\"13323232117\"," +
                "\"pwd\":\"12345678\"}";
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        given().
                header("Content-Type","application/json;charset=utf-8").
                header("X-Lemonban-Media-Type","lemonban.v1").
                body(jsonStr).
                when().
                post("http://api.lemonban.com/futureloan/member/login").
                then().
                log().body();
    }

}
