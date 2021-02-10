package com.test.day03.util;

import java.util.Random;

public class PhoneRandom {
    /**
     * 随机生成一个手机号码
     */
    public static String getPhone() {
        //定义手机号码号段
        String phonePrefix = "153";
        //后面8位随机生成
        Random random = new Random();
        int phoneLast = random.nextInt(99999999);
        String phoneNum = phonePrefix + phoneLast + "";
        Object result = JDBCUtils.querySingle("select count(*) from member where mobile_phone = " + phoneNum);
        while (true) {
            if (1 == (Long) result) {
                System.out.println("注册过了");
            } else {
                System.out.println("手机号码注册成功");
                return phoneNum;
            }
        }
    }

    public static void main(String[] args) {
    }
}
