package pipeline;

import Dao.MysqlDao;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import utils.ResultUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/29.
 * 多抽取项MYSQL管道
 */
public class MultiMysqlPipeline implements Pipeline {

    public void process(ResultItems resultItems, Task task) {
        List<Map<String, Object>> results = ResultUtils.parseResultItems(resultItems);

        if(results != null) {
            for (int i = 0; i < results.size(); i++) {
                String mark = results.size() == 1 ? "" : "-" + i;
                System.out.println("get page: " + resultItems.getRequest().getUrl() + mark);
                Map<String, Object> result = results.get(i);
                if(result !=null && result.size() > 0) {
                    String url = result.get("url").toString();
                    String author = result.get("Author").toString();
                    String content = result.get("Content").toString();
                    String time = result.get("Time").toString();
                    MysqlDao.insertDetail(url, author, content, time);
                }
            }
        }
    }
}
