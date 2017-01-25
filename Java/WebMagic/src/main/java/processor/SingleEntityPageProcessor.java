package processor;

import model.Expression;
import model.ExpressionType;
import model.ExtractField;
import model.FieldSourceType;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyPool;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;
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
public class SingleEntityPageProcessor extends CommonPageProcessor {

    public SingleEntityPageProcessor(){
        this.site = Site.me();
    }

    public void process(Page page) {

        // 部分二：从页面发现后续的url地址来抓取
        List<String> targetUrls = this.initTargetRequests(page, this.targetRequestCssSelector, this.targetRequestRegex);
        if(targetUrls != null) {
            page.addTargetRequests(targetUrls);
        }

        List<String> pagedUrls = this.initTargetRequests(page, this.pagedRequestCssSelector, this.pagedRequestRegex);
        if(pagedUrls != null) {
            page.addTargetRequests(pagedUrls);
        }

        // 部分三：定义如何抽取页面信息，并保存下来
        this.initExtractFields(page);

    }

    private List<String> initTargetRequests(Page page, String cssSelector, String regex){
        List<String> urls = null;

        if(this.expressions != null && !this.expressions.isEmpty()){
            Selectable linksSelectable = page.getHtml();

            for (Expression expression : expressions) {
                try {
                    Method method = ReflectionUtils.findMethodWithSuperClass(linksSelectable.getClass(), "css", expression.getArguments().length);
                    if(method != null) {
                        linksSelectable = (Selectable) method.invoke(linksSelectable, expression.getArguments());
                        System.out.println(linksSelectable);
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if(StringUtils.isNotEmpty(cssSelector) && StringUtils.isNotEmpty(regex)){
            urls = page.getHtml().css(cssSelector).links().regex(regex).all();
        }

        if(StringUtils.isNotEmpty(cssSelector)){
            urls = page.getHtml().css(cssSelector).links().all();
        }

        if(StringUtils.isNotEmpty(regex)){
            urls = page.getHtml().links().regex(regex).all();
        }

        return urls;
    }

    private void initExtractFields(Page page){
        if(this.extractFields != null){
            for (ExtractField field : extractFields) {
                if(field != null){
                    String fieldName = field.getFieldName();
                    ExpressionType expressionType = field.getExpressionType();
                    FieldSourceType fieldSourceType = field.getFieldSourceType();
                    Selector selector = field.getSelector();

                    Selectable extractContent = null;
                    if(fieldSourceType == FieldSourceType.Html){
                        extractContent = page.getHtml();
                    }else if(fieldSourceType == FieldSourceType.Url){
                        extractContent = page.getUrl();
                    }else if(fieldSourceType == FieldSourceType.Json){
                        extractContent = page.getJson();
                    }

                    if(extractContent != null && selector != null) {
                        if (field.isMulti()) {
                            List<String> results = page.getHtml().selectDocumentForList(field.getSelector());
                            if (field.isNeed() && results.size() == 0) {
                                //如果是必须字段，字段内容为空的时候跳过这页面
                                page.setSkip(true);
                            } else {
                                page.getResultItems().put(field.getFieldName(), results);
                            }
                        }else{
                            String result = extractContent.select(selector).toString();
                            if (field.isNeed() && result == null) {
                                //如果是必须字段，字段内容为空的时候跳过这页面
                                page.setSkip(true);
                            } else {
                                if (expressionType != ExpressionType.JsonPath) {
                                    page.putField(fieldName, extractContent.select(selector).toString());
                                } else {
                                    String expressionValue = field.getExpressionValue();
                                    if(StringUtils.isNotEmpty(expressionValue)) {
                                        page.putField(fieldName, extractContent.jsonPath(expressionValue).get());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
