package processor;

import model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyPool;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;
import utils.ExtractUtils;
import utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/12.
 */
public class DetailPageProcessor extends CommonPageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DetailPageProcessor(){
        this.site = Site.me();
    }

    public void process(Page page) {

        super.addUrlField(page.getResultItems().getAll(), page);
        super.extractTargetLinks(page);

        // 部分三：定义如何抽取页面信息，并保存下来
        this.extractFields(page);

    }

    private void extractFields(Page page){
        if(this.extractFields != null){
            this.extractFields.stream().filter(field -> field != null).forEach(field -> {
                String fieldName = field.getFieldName();
                ExpressionType expressionType = field.getExpressionType();
                FieldSourceType fieldSourceType = field.getFieldSourceType();
                Selector selector = field.getSelector();

                Selectable extractContent = ExtractUtils.getSelectable(page, fieldSourceType);

                if (extractContent != null && selector != null) {
                    if (field.isMulti()) {
                        List<String> results = page.getHtml().selectDocumentForList(selector);
                        if (field.isNeed() && results.size() == 0) {
                            //如果是必须字段，字段内容为空的时候跳过这页面
                            page.setSkip(true);
                        } else {
                            page.putField(fieldName, results);
                        }
                    } else {
                        if (expressionType != ExpressionType.JsonPath) {
                            String result = extractContent.select(selector).toString();
                            if (field.isNeed() && result == null) {
                                //如果是必须字段，字段内容为空的时候跳过这页面
                                page.setSkip(true);
                            } else {
                                page.putField(fieldName, result);
                            }
                        } else {
                            String expressionValue = field.getExpressionValue();
                            if (StringUtils.isNotEmpty(expressionValue)) {
                                String result = extractContent.jsonPath(expressionValue).get();
                                if (field.isNeed() && result == null) {
                                    //如果是必须字段，字段内容为空的时候跳过这页面
                                    page.setSkip(true);
                                } else {
                                    page.putField(fieldName, result);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
