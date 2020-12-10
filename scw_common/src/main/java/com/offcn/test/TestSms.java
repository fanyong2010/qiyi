package com.offcn.test;

import com.offcn.utils.HttpUtil;
import org.apache.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class TestSms {

    public static void main(String[] args) {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        // 替换成自己的appcode
        String appcode = "f22edecda923475b9fb7c504e9362408";

        Map<String, String> headers = new HashMap<String, String>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15638229363");
        querys.put("param", "code:9966");
        querys.put("tpl_id", "TP1711063");

        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtil.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
