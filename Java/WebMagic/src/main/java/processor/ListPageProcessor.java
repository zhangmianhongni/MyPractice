package processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selector;
import utils.ResultUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/29.
 */
public class ListPageProcessor extends CommonPageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ListPageProcessor(){
        this.site = Site.me();
    }

    public void process(Page page) {

        super.setMultiItemsPage(page, true);
        super.extractTargetLinks(page);

        // 部分三：定义如何抽取页面信息，并保存下来
        this.extractListFields(page);

    }

    private void extractListFields(Page page){

        List<Map<String, Object>> multiItems;
        Map<String, Object> mapList = new LinkedHashMap<>();

        if(this.extractFields != null){
            this.extractFields.stream().filter(field -> field != null).forEach(field -> {
                String fieldName = field.getFieldName();
                Selector selector = field.getSelector();
                Html extractContent = page.getHtml();

                if (extractContent != null && selector != null) {
                    List<String> results = extractContent.selectDocumentForList(selector);
                    if (field.isNeed() && results.size() == 0) {
                        //如果是必须字段，字段内容为空的时候跳过这页面
                        page.setSkip(true);
                    } else {
                        mapList.put(fieldName, results);
                    }
                }
            });
        }

        if(mapList.size() > 0){
            multiItems = ResultUtils.splitMapList(mapList);
            if(multiItems != null && mapList.size() > 0){
                multiItems.stream().filter(map -> map != null).forEach(map -> super.addUrlField(map, page));
                page.putField(ResultUtils.MULTI_ITEMS_STR, multiItems);
            }
        }
    }
}
