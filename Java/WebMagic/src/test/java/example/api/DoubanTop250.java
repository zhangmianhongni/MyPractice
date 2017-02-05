package example.api;

import constant.ExpressionType;
import constant.ExtractType;
import constant.FieldSourceType;
import model.Expression;
import model.ExtractField;
import model.LinksExtractRule;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import pipeline.MultiConsolePipeline;
import pipeline.MultiJsonFilePipeline;
import processor.ApiPageProcessor;
import processor.DetailPageProcessor;
import spider.CommonSpider;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import utils.ExtractUtils;

import javax.management.JMException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by mian on 2017/1/12.
 * 豆瓣电影TOP250
 */
public class DoubanTop250 {
    public static void main(String[] args) throws JMException {
        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName("title");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.title");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("genres");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.genres");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("averageRating");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.rating.average");
        extractFields.add(field);



        ApiPageProcessor processor = new ApiPageProcessor("$.subjects");
        processor.setRetryTimes(3);
        processor.setSleepTime(100);
        processor.setTimeOut(10000);

        List<LinksExtractRule> rules = new ArrayList<>();

        Integer onceCount = 10;
        NameValuePair[] nameValuePairs = {
                new BasicNameValuePair("start", "0"),
                new BasicNameValuePair("count", onceCount.toString()),
        };

        Map<String, String> extraValues = new HashMap<>();
        extraValues.put("start", onceCount.toString());

        LinksExtractRule rule = new LinksExtractRule();
        rule.setName("paged");
        rule.setExtractType(ExtractType.Api);
        rule.setRequestParams(nameValuePairs);
        rule.setRequestParamsExtra(extraValues);
        rules.add(rule);

        processor.setTargetRequestRules(rules);
        processor.setExtractFields(extractFields);

        String startUrl = "https://api.douban.com/v2/movie/top250";
        startUrl = ExtractUtils.getUrlByParamsMap(startUrl, nameValuePairs);

        CommonSpider spider = CommonSpider.create(processor);
        spider.addUrl(startUrl)
                .addPipeline(new MultiConsolePipeline())
                .addPipeline(new MultiJsonFilePipeline("D:\\webmagic\\"))
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
