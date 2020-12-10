package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Data
public class OssUtil {

    private String endpoint;
    private String bucketDomain;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 返回上传后的文件访问路径
     *
     * @param is
     * @param fileName
     * @return
     * @throws IOException
     */
    public String upload(InputStream is, String fileName) {

        //1、加工文件夹和文件名
        // 创建一个日期格式的目录：如2020-12-03
        String folderName = DateUtil.getFormatTime("yyyy-MM-dd");
        fileName = UUID.randomUUID().toString().replace("-", "") + "_" + fileName;

        //2、创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //3、// 上传文件流，指定bucket的名称
        ossClient.putObject(bucketName, "pic/" + folderName + "/" + fileName, is);

        //4、关闭资源
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();

        return "http://" + bucketDomain + "/pic/" + folderName + "/" + fileName;
    }

}
