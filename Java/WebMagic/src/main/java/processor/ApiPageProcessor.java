package processor;

import constant.FieldSourceType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selector;
import utils.ResultUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/29.
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * API抽取类
 */
public class ApiPageProcessor extends CommonPageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String dataJsonPath = "$";

    public ApiPageProcessor(){
        super();
    }

    public ApiPageProcessor(String dataJsonPath){
        super();
        if(StringUtils.isNotEmpty(dataJsonPath)) {
            this.dataJsonPath = dataJsonPath;
        }
    }

    @Override
    public void doProcess(Page page) {
        super.setMultiItemsPage(page, true);

        // 部分三：定义如何抽取页面信息，并保存下来
        int resultsCount = this.extractJsonFields(page);
        if(resultsCount > 0){
            super.extractTargetLinks(page);
        }
    }

    private int extractJsonFields(Page page){
        int resultsCount = 0;
        List<Map<String, Object>> multiItems = new ArrayList<>();
        if(page.getJson() != null && page.getJson().jsonPath(this.dataJsonPath) != null) {
            Json dataJson = new Json(page.getJson().jsonPath(this.dataJsonPath).all());

            if (this.extractFields != null && dataJson.all().size() > 0) {
                for (String jsonStr : dataJson.all()) {
                    Map<String, Object> fieldMap = new LinkedHashMap<>();
                    Json json = new Json(jsonStr);
                    this.extractFields.stream().filter(field -> field != null && field.getFieldSourceType() == FieldSourceType.Json).forEach(field -> {
                        String fieldName = field.getFieldName();
                        String expressionValue = field.getExpressionValue();

                        if (StringUtils.isNotEmpty(expressionValue)) {
                            List<String> results = json.jsonPath(expressionValue).all();
                            if(results.size() > 0) {
                                fieldMap.put(fieldName, results.size() > 1 ? results : results.get(0));
                            }
                        }
                    });

                    multiItems.add(fieldMap);
                }
            }
        }

        if(multiItems.size() > 0){
            resultsCount = multiItems.size();
            multiItems.stream().filter(map -> map != null).forEach(map -> super.addUrlField(map, page));
            page.putField(ResultUtils.MULTI_ITEMS_STR, multiItems);
        }

        return resultsCount;
    }
}
