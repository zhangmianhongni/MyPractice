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

        LocalDateTime start = LocalDateTime.now();
        Spider spider = Spider.create(processor)
                //从"https://api.douban.com/v2/movie/top250"开始抓
                .addUrl("https://api.douban.com/v2/book/1220562")
                .addPipeline(new MultiConsolePipeline())
                //保存到JSON文件
                .addPipeline(new MultiJsonFilePipeline("D:\\webmagic\\"))
                .thread(5);

        SpiderMonitor.instance().register(spider);
        spider.run();

        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        System.out.println(duration.getSeconds());
    }
}
