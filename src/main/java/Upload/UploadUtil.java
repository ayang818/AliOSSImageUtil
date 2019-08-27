package Upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Date;

public class UploadUtil {
    private static String endpoint = "<你的Bucket的地址域名>";
    private static String accessKeyId = "<你的accessKeyId>";
    private static String accessKeySecret = "<你的accessKeySecret>";

    private static String bucketName = "<已经创建bucket>";


    private OSS ossClient;

    public UploadUtil() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public void destory() {
        ossClient.shutdown();
    }

    public void uploadImage(String filedir, String filepath) {
        try {
            File file = new File(filepath);
            String[] filenameList = filepath.split("/");
            String filePath = filedir + "/" + filenameList[filenameList.length - 1];
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(file);
            this.uploadFileToOss(fileInputStream, filePath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void uploadImage(File file, String filedir) {
        String filename = file.getName();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String filepath = filedir + "/" + filename;
            this.uploadFileToOss(fileInputStream, filepath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void uploadFileToOss(FileInputStream fileInputStream, String imagePath) {
        ossClient.putObject(bucketName, imagePath, fileInputStream);
        System.out.println("upload success!");
    }


    public String getUrl(String filePath) {
        // 设置url过期时间
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
        URL url = ossClient.generatePresignedUrl(bucketName, filePath, expiration);
        if (url != null) {
            String urlString = url.toString();
            return urlString;
        }
        return null;
    }
}
