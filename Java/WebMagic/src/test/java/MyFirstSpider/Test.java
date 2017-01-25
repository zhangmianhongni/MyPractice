package myFirstSpider;

import model.Expression;
import model.ExtractField;
import model.ExpressionType;
import model.FieldSourceType;
import processor.CommonPageProcessor;
import processor.SingleEntityPageProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by mian on 2017/1/12.
 */
public class Test {
    public static void main(String[] args) {
        //Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/zhangmianhongni").thread(5).run();


        List<ExtractField> extractFields = new ArrayList<ExtractField>();

        ExtractField field = new ExtractField();
        field.setFieldName("Author");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//a[@class='article-author-name']/text()");
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



        SingleEntityPageProcessor processor = new SingleEntityPageProcessor();
        processor.setRetryTimes(3);
        processor.setSleepTime(100);
        processor.setTimeOut(10000);

        Expression expression = new Expression();
        expression.setExpressionType(ExpressionType.Css);
        expression.setArguments(new Object[] {"div.pager-content"});
        processor.setTargetRequestExpressions(Arrays.asList(expression));

        processor.setTargetRequestsUrl("div.pager-content", null);
        processor.setExtractFields(extractFields);

        Spider.create(processor)
                //从"http://baozoumanhua.com/text"开始抓
                .addUrl("http://baozoumanhua.com/text")
                .addPipeline(new ConsolePipeline())
                //保存到JSON文件
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                .thread(5)
                .start();
    }
}
