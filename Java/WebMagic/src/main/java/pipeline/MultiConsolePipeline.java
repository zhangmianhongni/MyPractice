package pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import utils.ResultUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/29.
 * 多抽取项控制台管道
 */
public class MultiConsolePipeline implements Pipeline {

    public void process(ResultItems resultItems, Task task) {
        List<Map<String, Object>> results = ResultUtils.parseResultItems(resultItems);

        if(results != null) {
            for (int i = 0; i < results.size(); i++) {
                String mark = results.size() == 1 ? "" : "-" + i;
                System.out.println("get page: " + resultItems.getRequest().getUrl() + mark);
                Map<String, Object> result = results.get(i);
                if(result !=null && result.size() > 0) {
                    for (Map.Entry<String, Object> entry : result.entrySet()) {
                        System.out.println(entry.getKey() + ":\t" + entry.getValue());
                    }
                }
            }
        }
    }
}
