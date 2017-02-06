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
import spider.CommonSpider;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by mian on 2017/1/12.
 * 暴走漫画 http://baozoumanhua.com/text/fresh?page=1
 */
public class Baozou {
    public static void main(String[] args) throws JMException {

        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName("Author");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//[@class='article-author-name']/text()");
        field.setNeed(true);
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("Content");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//div[@class='article article-text']/@data-text");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("Time");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//span[@class='article-date']/text()");
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

        String startUrl = "http://baozoumanhua.com/text/fresh?page=1";
        CommonSpider spider = CommonSpider.create(processor);
        spider.addUrl(startUrl)
                .addPipeline(new MultiConsolePipeline())
                .addPipeline(new MultiJsonFilePipeline("D:\\webmagic\\"))
                .addPipeline(new MultiMysqlPipeline())
                .thread(5);

        SpiderMonitor.instance().register(spider);
        spider.run();

        System.out.println("请求页面数量：" + spider.getPageCount());
        System.out.println("请求成功页面数量：" + ((SpiderMonitor.MonitorSpiderListener)spider.getSpiderListeners().get(0)).getSuccessCount());
        System.out.println("请求失败页面数量：" + ((SpiderMonitor.MonitorSpiderListener)spider.getSpiderListeners().get(0)).getErrorCount());
        System.out.println("请求失败页面：" + ((SpiderMonitor.MonitorSpiderListener)spider.getSpiderListeners().get(0)).getErrorUrls());


        System.out.println("Processor处理页面数量：" + spider.getProcessPageCount());
        System.out.println("Pipeline处理页面数量：" + spider.getPipelinePageCount());

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.ofInstant(spider.getStartTime().toInstant(), TimeZone.getDefault().toZoneId());
        Duration duration = Duration.between(start, end);
        System.out.println("爬虫捉取时间：" + duration.getSeconds() + "秒");
    }
}
