package pipeline;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.CommonPageProcessor;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;
import utils.ResultUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/26.
 * 多Json文件管道
 */
public class MultiJsonFilePipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MultiJsonFilePipeline() {
        this.setPath("/data/webmagic");
    }

    public MultiJsonFilePipeline(String path) {
        this.setPath(path);
    }

    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;

        List<Map<String, Object>> results = ResultUtils.parseResultItems(resultItems);
        if(results != null) {
            try {
                for (int i = 0; i < results.size(); i++) {
                    String mark = results.size() == 1 ? "" : "-" + i;
                    Map<String, Object> result = results.get(i);
                    if(result != null && result.size() > 0) {
                        PrintWriter e = new PrintWriter(new FileWriter(this.getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + mark + ".json")));
                        e.write(JSON.toJSONString(result));
                        e.close();
                    }
                }
            } catch (IOException var5) {
                this.logger.warn("write file error", var5);
            }
        }
    }
}
