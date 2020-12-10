package com.offcn.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestOss {

    public static void main(String[] args) throws FileNotFoundException {

        // Endpoint：北京
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAI4GA1yuvoe9eNUf2eRrGf";
        String accessKeySecret = "T30Y7U3QEEO3exXJttWXxaNWYgH1dA";

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流
        InputStream inputStream = new FileInputStream(new File("E:\\images\\ly1.jpg"));

        // 第一个参数：bucketName
        // 第二个参数：文件上传到oss的路径（上传到哪个目录）
        // 第三个参数：上传的文件流
        ossClient.putObject("offcn666", "pic/cc.jpg", inputStream);

        // 关闭OSSClient
        ossClient.shutdown();

        System.out.println("测试完成");
    }

}
