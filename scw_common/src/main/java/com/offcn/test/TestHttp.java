package com.offcn.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TestHttp {

    // 通过代码的方式，请求别人的系统
    // 刷票   爬虫   暴力破解（刷用户名密码）
    public static void main(String[] args) throws IOException {

        while(true) {
            // 创建一个客户端对象
            HttpClient client = new DefaultHttpClient();

            // 创建一个请求对象
            HttpGet get = new HttpGet("https://www.baidu.com/");

            // 发送请求，获得响应结果
            HttpResponse resp = client.execute(get);

            // 获取响应码
            int statusCode = resp.getStatusLine().getStatusCode();
            String result = EntityUtils.toString(resp.getEntity());

            System.out.println("响应码是：" + statusCode);
            System.out.println("响应结果是：" + result);
        }

    }

}
