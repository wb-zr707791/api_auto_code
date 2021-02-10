package com.test.day02;

import io.restassured.response.Response;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class CookieTest {
    /**
     * cookie+session的鉴权方式
     */
    @Test
    public void testAuthenticationWithSession() {
        Response response =
        given().
                header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                formParam("loginame", "admin").formParam("password", "e10adc3949ba59abbe56e057f20f883e").
        when().
                post("http://erp.lemfix.com/user/login").
                then().log().all().extract().response();
        System.out.println(response.getCookies());
        given().
                cookies(response.getCookies()).
                when().
                get("http://erp.lemfix.com/user/getUserSession").
                then().log().all();

    }
}
