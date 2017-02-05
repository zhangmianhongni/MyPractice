package pipeline;

import dao.MysqlDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import utils.ExtractUtils;
import utils.ResultUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/29.
 * 多抽取项MYSQL管道
 */
public class MultiMysqlPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void process(ResultItems resultItems, Task task) {
        List<Map<String, Object>> results = ResultUtils.parseResultItems(resultItems);

        if(results != null) {
            for (int i = 0; i < results.size(); i++) {
                String mark = results.size() == 1 ? "" : "-" + i;
                Map<String, Object> result = results.get(i);
                if(result != null && result.size() > 0) {
                    String url = result.get("url").toString();
                    String sourceUrl = result.get(ExtractUtils.SOURCE_REQ_URL_STR).toString();
                    String author = result.get(ExtractUtils.IMAGE_NAME_STR).toString();
                    String content = result.get(ExtractUtils.IMAGE_SOURCE_STR).toString();
                    //String time = result.get("Time").toString();
                    MysqlDao.insertDetail(sourceUrl, url, author, content, "");
                }
            }
        }
    }
}
