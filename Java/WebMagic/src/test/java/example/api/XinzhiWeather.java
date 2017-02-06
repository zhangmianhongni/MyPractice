package example.api;

import constant.ExtractType;
import constant.FieldSourceType;
import model.ExtractField;
import model.LinksExtractRule;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import pipeline.MultiConsolePipeline;
import pipeline.MultiJsonFilePipeline;
import processor.ApiPageProcessor;
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
 * 心知天气，广州从今日开始连续3日天气情况
 */
public class XinzhiWeather {
    public static void main(String[] args) throws JMException {
        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName("date");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.date");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("high");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.high");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("low");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.low");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("text_day");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.text_day");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("text_night");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.text_night");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("wind_direction");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.wind_direction");
        extractFields.add(field);



        ApiPageProcessor processor = new ApiPageProcessor("$.results[0].daily");
        processor.setRetryTimes(3);
        processor.setSleepTime(100);
        processor.setTimeOut(10000);

        List<LinksExtractRule> rules = new ArrayList<>();

        NameValuePair[] nameValuePairs = {
                new BasicNameValuePair("key", "vueixasxvhoscixt"),
                new BasicNameValuePair("location", "guangzhou"),
                new BasicNameValuePair("language", "zh-Hans"),
                new BasicNameValuePair("unit", "c"),
                new BasicNameValuePair("start", "0"),
                new BasicNameValuePair("days", "3"),
        };

        LinksExtractRule rule = new LinksExtractRule();
        rule.setName("paged");
        rule.setExtractType(ExtractType.Api);
        rule.setRequestParams(nameValuePairs);
        rules.add(rule);

        processor.setTargetRequestRules(rules);
        processor.setExtractFields(extractFields);

        String startUrl = "https://api.thinkpage.cn/v3/weather/daily.json";
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
