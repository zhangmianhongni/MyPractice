package processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import utils.ResultUtils;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/12.
 */
public class DetailPageProcessor extends CommonPageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DetailPageProcessor(){
        super();
    }

    @Override
    public void doProcess(Page page) {
        super.addUrlField(page.getResultItems().getAll(), page);
        super.extractTargetLinks(page);

        // 部分三：定义如何抽取页面信息，并保存下来
        ResultUtils.extractDetailFields(page, this.extractFields);
    }
}
