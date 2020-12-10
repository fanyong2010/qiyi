package com.offcn.test;

public class TestInteger {

    public static void main(String[] args) {

        // 包装类：其中维护了一个 -128 127 的缓存
        Integer a = 127;
        Integer b = 127;

        Integer c = 200;
        Integer d = 200;
                                    //          //           // 2个人
        System.out.println(a == b); // false    // true     // true
        System.out.println(c == d); // false    // true     // false

    }

}
