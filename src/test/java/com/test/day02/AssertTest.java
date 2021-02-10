package com.test.day02;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AssertTest {
    @Test
    public void login() {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("mobile_phone", "13323232117");
        map1.put("pwd", "12345678");
        //如果form表单参数有中文的化，记得添加charset=utf-8到content-type里面，否则有乱码问题
        Response response =
        given().
                header("Content-Type", "application/json;charset=utf-8").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                body(map1).
        when().
                post("http://api.lemonban.com/futureloan/member/login").
        then().
                extract().response();
        Object code = response.path("code");
        Object msg = response.path("msg");
        Object phone = response.path("data.mobile_phone");
        Assert.assertEquals(code,0,"断言失败");
        Assert.assertEquals(msg,"OK","断言失败");
        Assert.assertEquals(phone,map1.get("mobile_phone"),"断言失败");
    }
}
