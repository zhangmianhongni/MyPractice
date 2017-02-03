package processor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import utils.ExtractUtils;
import utils.ResultUtils;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/12.
 */
public class ListWithDetailPageProcessor extends CommonPageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String listLinksRegExp;

    public ListWithDetailPageProcessor(String listLinksRegExp){
        super();
        this.listLinksRegExp = listLinksRegExp;
    }

    @Override
    public void doProcess(Page page) {
        //如果是列表页则只抽取链接，详情页才抽取具体字段内容
        if (page.getUrl().regex(this.listLinksRegExp).match()) {
            super.extractTargetLinks(page);
        } else {
            String sourceUrl = page.getRequest().getExtra(ExtractUtils.SOURCE_REQ_URL_STR).toString();
            if(StringUtils.isNotEmpty(sourceUrl)){
                page.putField(ExtractUtils.SOURCE_REQ_URL_STR, sourceUrl);
            }
            super.addUrlField(page.getResultItems().getAll(), page);
            ResultUtils.extractDetailFields(page, this.extractFields);
        }
    }
}
