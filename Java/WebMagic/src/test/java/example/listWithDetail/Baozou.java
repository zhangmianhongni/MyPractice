package example.listWithDetail;

import constant.ExpressionType;
import constant.FieldSourceType;
import model.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import pipeline.MultiConsolePipeline;
import pipeline.MultiJsonFilePipeline;
import pipeline.MultiMysqlPipeline;
import processor.ListWithDetailPageProcessor;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/12.
 */
public class Baozou {
    public static void main(String[] args) throws JMException {

        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName("Author");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//[@class='article-author-name']/text()");
        //field.setMulti(true);
        field.setNeed(true);

        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("Content");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//div[@class='article article-text']/@data-text");
        //field.setMulti(true);

        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("Time");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//span[@class='article-date']/text()");
        //field.setMulti(true);

        extractFields.add(field);


        //列表链接正则表达式，用来判断是列表的链接还是详情页的链接
        String listLinksRegExp = "http://baozoumanhua\\.com/text";

        ListWithDetailPageProcessor processor = new ListWithDetailPageProcessor(listLinksRegExp);
        processor.setRetryTimes(3);
        processor.setSleepTime(200);
        processor.setTimeOut(30000);

        List<LinksExtractRule> rules = new ArrayList<>();

        //详情页面链接规则
        List<Expression> expressions = new ArrayList<>();
        Expression expression = new Expression();
        expression.setExpressionType(ExpressionType.Css);
        expression.setArguments(new Object[] {"div.articles"});
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Links);
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Regex);
        expressions.add(expression);
        expression.setArguments(new Object[] {"http://baozoumanhua\\.com/articles/\\d+"});

        LinksExtractRule rule = new LinksExtractRule();
        rule.setName("detail");
        rule.setExpressions(expressions);
        rules.add(rule);

        //分页链接规则
        expressions = new ArrayList<>();
        expression = new Expression();
        expression.setExpressionType(ExpressionType.Css);
        expression.setArguments(new Object[] {"div.pager-content"});
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Links);
        expressions.add(expression);

        rule = new LinksExtractRule();
        rule.setName("paged");
        rule.setExpressions(expressions);
        rules.add(rule);

        processor.setTargetRequestRules(rules);
        processor.setExtractFields(extractFields);

        String url = "http://baozoumanhua.com/text/fresh?page=1";

        LocalDateTime start = LocalDateTime.now();
        Spider spider = Spider.create(processor)
                //从"http://baozoumanhua.com/text"开始抓
                .addUrl(url)
                .addPipeline(new MultiConsolePipeline())
                //保存到JSON文件
                .addPipeline(new MultiJsonFilePipeline("D:\\webmagic\\"))
                .addPipeline(new MultiMysqlPipeline())
                .thread(5);

        SpiderMonitor.instance().register(spider);
        spider.run();

        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        System.out.println(duration.getSeconds());
    }
}
