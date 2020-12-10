package com.offcn.user.util;

import com.offcn.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
// 替换类中
// private final Logger log = LoggerFactory.getLogger(当前类名.class);
@Slf4j
public class SmsUtil {

    @Value("${sms.host}")
    private String host;
    @Value("${sms.path}")
    private String path;
    @Value("${sms.method}")
    private String method;
    @Value("${sms.appcode}")
    private String appcode;
    @Value("${sms.tplid}")
    private String tplid;

    public String sendCode(String phoneNo, String code) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNo);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", tplid);

        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtil.doPost(host, path, method, headers, querys, bodys);

            String result = EntityUtils.toString(response.getEntity());

            log.info("短信发送完成；手机号：{}，验证码：{}", phoneNo, code);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("短信发送失败；发送参数是：{}", querys);
            return "fail";
        }
    }

}
