package com.test.day01;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FutureTokenTest {
    @Test
    public void testLogin() {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("mobile_phone", "13323232117");
        map1.put("pwd", "12345678");
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        Response response = given().
                header("Content-Type", "application/json;charset=utf-8").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                body(map1).
                when().
                post("http://api.lemonban.com/futureloan/member/login").
                then().
                extract().response();
//        System.out.println(response.asString());
//        System.out.println(response.statusCode());
        //提取token
        Object token = response.path("data.token_info.token");
        Object member_id = response.path("data.id");
        //把请求数据放到map
        Map<String, String> map = new HashMap<String, String>();
        map.put("member_id",member_id.toString());
        map.put("amount", "1");
        given().
                header("Content-Type", "application/json;charset=utf-8").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization","Bearer "+token).
                body(map).
                when().
                post("http://api.lemonban.com/futureloan/member/recharge").
                then().log().all();
    }

}

