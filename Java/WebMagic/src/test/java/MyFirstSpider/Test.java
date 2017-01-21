package myFirstSpider;

import model.ExtractField;
import model.ExpressionType;
import model.FieldSourceType;
import processor.CommonPageProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mian on 2017/1/12.
 */
public class Test {
    public static void main(String[] args) {
        //Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/zhangmianhongni").thread(5).run();


        List<ExtractField> extractFields = new ArrayList<ExtractField>();
        ExtractField field = new ExtractField();
        field.setFieldName("author");
        field.setFieldSourceType(FieldSourceType.Url);
        field.setExpressionType(ExpressionType.Regex);
        field.setExpressionValue("https://github\\.com/(\\w+)/.*");

        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("name");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//h1[@class='public']/strong/a/text()");
        field.setNeed(true);

        extractFields.add(field);

        field = new ExtractField();
        field.setFieldName("readme");
        field.setFieldSourceType(FieldSourceType.Html);
        field.setExpressionType(ExpressionType.XPath);
        field.setExpressionValue("//div[@id='readme']/tidyText()");

        extractFields.add(field);



        CommonPageProcessor processor = new CommonPageProcessor();
        processor.setRetryTimes(3);
        processor.setSleepTime(100);
        processor.setTimeOut(10000);
        processor.setTargetRequests("div.js-pinned-repos-reorder-container", "(https://github\\.com/zhangmianhongni/\\w+)");
        processor.setExtractFields(extractFields);

        Spider.create(processor)
                //从"https://github.com/zhangmianhongni"开始抓
                .addUrl("https://github.com/zhangmianhongni")
                .addPipeline(new ConsolePipeline())
                //保存到JSON文件
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                .thread(5)
                .start();
    }
}
