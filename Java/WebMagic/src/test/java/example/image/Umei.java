package example.image;

import constant.ExpressionType;
import constant.FieldSourceType;
import model.Expression;
import model.ExtractField;
import model.LinksExtractRule;
import pipeline.ImagePipeline;
import pipeline.MultiConsolePipeline;
import pipeline.MultiMysqlPipeline;
import processor.ListWithDetailPageProcessor;
import spider.CommonSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import utils.ExtractUtils;

import javax.management.JMException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mian on 2017/1/12.
 * 优美 https://www.umei.cc/tags/juru_1.htm
 */
public class Umei {
    public static void main(String[] args) throws JMException {

        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName(ExtractUtils.IMAGE_NAME_STR);
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//div[@class='ImageBody']//img/@alt");
        field.setNeed(true);
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName(ExtractUtils.IMAGE_SOURCE_STR);
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//div[@class='ImageBody']//img/@src");
        extractFields.add(field);


        //列表链接正则表达式，用来判断是列表的链接还是详情页的链接
        String listLinksRegExp = "https://www\\.umei\\.cc/tags/juru_\\d+.htm";

        ListWithDetailPageProcessor processor = new ListWithDetailPageProcessor(listLinksRegExp);
        processor.setRetryTimes(3);
        processor.setRetrySleepTime(500);
        processor.setSleepTime(200);
        processor.setTimeOut(10000);

        List<LinksExtractRule> rules = new ArrayList<>();

        //详情页面链接规则
        List<Expression> expressions = new ArrayList<>();
        Expression expression = new Expression();
        expression.setExpressionType(ExpressionType.Css);
        expression.setArguments(new Object[] {"div.TypeList"});
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Links);
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Regex);
        expressions.add(expression);
        expression.setArguments(new Object[] {"https://www\\.umei\\.cc/[/\\w\\.-]*/\\d+.htm"});

        LinksExtractRule rule = new LinksExtractRule();
        rule.setName("detail");
        rule.setExpressions(expressions);
        rules.add(rule);

        //分页链接规则
        expressions = new ArrayList<>();
        expression = new Expression();
        expression.setExpressionType(ExpressionType.Css);
        expression.setArguments(new Object[] {"div.NewPages"});
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Links);
        expressions.add(expression);

        expression = new Expression();
        expression.setExpressionType(ExpressionType.Regex);
        expressions.add(expression);
        expression.setArguments(new Object[] {"https://www\\.umei\\.cc/tags/juru_\\d+.htm"});

        rule = new LinksExtractRule();
        rule.setName("paged");
        rule.setExpressions(expressions);
        rules.add(rule);

        processor.setTargetRequestRules(rules);
        processor.setExtractFields(extractFields);

        String startUrl = "https://www.umei.cc/tags/juru_1.htm";
        CommonSpider spider = CommonSpider.create(processor);
        spider.addUrl(startUrl)
                .addPipeline(new MultiConsolePipeline())
                //保存图片文件
                .addPipeline(new ImagePipeline("D:\\webmagic\\"))
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
