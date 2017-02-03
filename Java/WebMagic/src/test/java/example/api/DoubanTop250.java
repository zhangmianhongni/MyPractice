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

        LocalDateTime start = LocalDateTime.now();
        Spider spider = Spider.create(processor)
                //从"https://api.douban.com/v2/movie/top250"开始抓
                .addUrl("https://api.douban.com/v2/movie/top250?start=0&count=10")
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
