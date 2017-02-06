package example.api;

import constant.FieldSourceType;
import model.ExtractField;
import pipeline.MultiConsolePipeline;
import pipeline.MultiJsonFilePipeline;
import processor.ApiPageProcessor;
import spider.CommonSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by mian on 2017/1/12.
 * 豆瓣ID为1220562的书的信息
 */
public class DoubanBookDetail {
    public static void main(String[] args) throws JMException {
        List<ExtractField> extractFields = new ArrayList<>();

        ExtractField field = new ExtractField();
        field.setFieldName("title");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.title");
        field.setNeed(true);
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("publisher");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.publisher");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("author");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.author");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("pubdate");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.pubdate");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("summary");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.summary");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("price");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.price");
        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("numRaters");
        field.setFieldSourceType(FieldSourceType.Json);
        field.setExpressionValue("$.rating.numRaters");
        extractFields.add(field);


        ApiPageProcessor processor = new ApiPageProcessor();
        processor.setRetryTimes(3);
        processor.setSleepTime(100);
        processor.setTimeOut(10000);
        processor.setExtractFields(extractFields);

        String startUrl = "https://api.douban.com/v2/book/1220562";
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
