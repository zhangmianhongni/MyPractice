package pipeline;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;
import utils.ExtractUtils;
import utils.ResultUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/2/4.
 * 图片文件管道
 */

public class ImagePipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ImagePipeline() {
        setPath("/data/webmagic/");
    }

    public ImagePipeline(String path) {
        setPath(path);
    }


    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;

        List<Map<String, Object>> results = ResultUtils.parseResultItems(resultItems);
        if(results != null) {
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();

                for (int i = 0; i < results.size(); i++) {
                    String mark = results.size() == 1 ? "" : "-" + i;
                    Map<String, Object> result = results.get(i);
                    if(result != null && result.size() > 0) {
                        String imageName = result.get(ExtractUtils.IMAGE_NAME_STR).toString();
                        String imgUrl = result.get(ExtractUtils.IMAGE_SOURCE_STR).toString();
                        StringBuilder sb = new StringBuilder();

                        String extName = com.google.common.io.Files.getFileExtension(imgUrl);
                        if (StringUtils.isEmpty(extName)) {
                            extName = "png";
                        }

                        StringBuilder imgFileName = sb.append(path).append(imageName).append(mark).append(".").append(extName);

                        //这里通过httpclient下载之前抓取到的图片网址，并放在对应的文件中
                        HttpGet httpget = new HttpGet(imgUrl);
                        HttpResponse response = httpclient.execute(httpget);
                        if (response != null) {
                            InputStream inputStream = response.getEntity().getContent();

                            if (inputStream != null) {
                                FileOutputStream fos = new FileOutputStream(this.getFile(imgFileName.toString()));
                                int len;
                                byte[] data = new byte[1024];
                                while ((len = inputStream.read(data)) != -1) {
                                    fos.write(data, 0, len);
                                }
                                fos.flush();
                                fos.close();
                                inputStream.close();
                            }
                        } else {
                            logger.warn("下载图片{}失败！", imgUrl);
                        }
                    }
                }
                httpclient.close();
            } catch (IOException e) {
                logger.warn("write file error", e);
            }
        }
    }
}
