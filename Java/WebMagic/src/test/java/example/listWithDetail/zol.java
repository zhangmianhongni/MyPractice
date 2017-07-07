package example.listWithDetail;

import constant.ExpressionType;
import constant.FieldSourceType;
import model.Expression;
import model.ExtractField;
import model.LinksExtractRule;
import pipeline.MultiConsolePipeline;
import pipeline.MultiJsonFilePipeline;
import pipeline.MultiMysqlPipeline;
import processor.ListWithDetailPageProcessor;
import spider.CommonSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mian on 2017/1/12.
 * 中关村 http://detail.zol.com.cn/cell_phone_index/subcate57_34645_list_1.html
 */
public class zol {
    public static void main(String[] args) throws JMException {

        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName("title");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("/html/body/div[4]/div[7]/h1/text()");
        field.setNeed(true);
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("price");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//*[@id=\"J_PriceTrend\"]/b[2]/text()");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("size");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("/html/body/div[7]/div[1]/div[7]/div[2]/ul/li[1]/p[1]/span[2]/text()");
        extractFields.add(field);


        //列表链接正则表达式，用来判断是列表的链接还是详情页的链接
        String listLinksRegExp = "http://detail\\.zol\\.com\\.cn/cell_phone_index/";

        ListWithDetailPageProcessor processor = new ListWithDetailPageProcessor(listLinksRegExp);
        processor.setRetryTimes(3);
        processor.setSleepTime(200);
        processor.setTimeOut(30000);

        List<LinksExtractRule> rules = new ArrayList<>();

        //详情页面链接规则
        List<Expression> expressions = new ArrayList<>();
        Expression expression = new Expression();
        expression.setExpressionType(ExpressionType.XPath);
        expressions.add(expression);
        expression.setArguments(new Object[] {"//ul[@id='J_PicMode']//li//h3/a/@href"});

        LinksExtractRule rule = new LinksExtractRule();
        rule.setName("detail");
        rule.setExpressions(expressions);
        rules.add(rule);

        //分页链接规则
        expressions = new ArrayList<>();
        expression = new Expression();
        expression.setExpressionType(ExpressionType.Css);
        expression.setArguments(new Object[] {"div.pagebar"});
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

        String startUrl = "http://detail.zol.com.cn/cell_phone_index/subcate57_list_1.html";
        CommonSpider spider = CommonSpider.create(processor);
        spider.addUrl(startUrl)
                .addPipeline(new MultiConsolePipeline())
                .addPipeline(new MultiJsonFilePipeline("D:\\webmagic\\"))
                //.addPipeline(new MultiMysqlPipeline())
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
