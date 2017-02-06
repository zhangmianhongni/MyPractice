package utilsTest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mian on 2017/2/4.
 * 下载图片测试
 */
public class ImageTest {
    public static void main(String[] args) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://img.51ztzj.com//upload/image/20160724/201607247_670x419.jpg");
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            if(response != null) {
                InputStream inputStream = response.getEntity().getContent();

                if(inputStream != null) {
                    File file = new File("D:\\webmagic\\aa.jpg");

                    FileOutputStream fos = new FileOutputStream(file);
                    int len = 0;
                    byte[] data = new byte[1024];
                    while ((len = inputStream.read(data)) != -1) {
                        fos.write(data, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
