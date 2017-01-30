package processor;

import model.ExpressionType;
import model.ExtractField;
import model.FieldSourceType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;
import utils.ExtractUtils;
import utils.ResultUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/12.
 */
public class ListWithDetailPageProcessor extends CommonPageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String listLinksRegExp;

    //todo 测试用
    public List<String> linkList = new ArrayList<>();
    public List<String> detailList = new ArrayList<>();

    public ListWithDetailPageProcessor(String listLinksRegExp){
        this.site = Site.me();
        this.listLinksRegExp = listLinksRegExp;
    }

    public void process(Page page) {

        //如果是列表页则只抽取链接，详情页才抽取具体字段内容
        if (page.getUrl().regex(this.listLinksRegExp).match()) {
            super.extractTargetLinks(page);
            linkList.add(page.getUrl().toString());
        } else {
            String sourceUrl = page.getRequest().getExtra(ExtractUtils.SOURCE_REQUEST_URL_STR).toString();
            if(StringUtils.isNotEmpty(sourceUrl)){
                page.putField(ExtractUtils.SOURCE_REQUEST_URL_STR, sourceUrl);
            }
            super.addUrlField(page.getResultItems().getAll(), page);
            ResultUtils.extractDetailFields(page, this.extractFields);
            detailList.add(page.getUrl().toString());
        }
    }
}
