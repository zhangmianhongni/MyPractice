package pipeline;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;
import utils.ResultUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/28.
 * 多文件管道
 */
@ThreadSafe
public class MultiFilePipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MultiFilePipeline() {
        this.setPath("/data/webmagic/");
    }

    public MultiFilePipeline(String path) {
        this.setPath(path);
    }

    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;

        List<Map<String, Object>> results = ResultUtils.parseResultItems(resultItems);
        if(results != null) {
            try {
                for (int i = 0; i < results.size(); i++) {
                    String mark = results.size() == 1 ? "" : "-" + i;
                    PrintWriter e = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + mark  + ".html")), "UTF-8"));
                    e.println("url:\t" + resultItems.getRequest().getUrl());
                    Map<String, Object> result = results.get(i);
                    Iterator var5 = result.entrySet().iterator();

                    while (true) {
                        while (var5.hasNext()) {
                            Map.Entry entry = (Map.Entry) var5.next();
                            if (entry.getValue() instanceof Iterable) {
                                Iterable value = (Iterable) entry.getValue();
                                e.println((String) entry.getKey() + ":");
                                Iterator var8 = value.iterator();

                                while (var8.hasNext()) {
                                    Object o = var8.next();
                                    e.println(o);
                                }
                            } else {
                                e.println((String) entry.getKey() + ":\t" + entry.getValue());
                            }
                        }

                        e.close();
                        break;
                    }
                }
            } catch (IOException var10) {
                this.logger.warn("write file error", var10);
            }
        }

    }
}
