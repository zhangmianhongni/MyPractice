package spider;

import processor.CommonPageProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by mian on 2017/2/5.
 * 继承Spider，扩展Processor处理页面数量
 */
public class CommonSpider extends Spider {

    public static CommonSpider create(CommonPageProcessor pageProcessor) {
        return new CommonSpider(pageProcessor);
    }

    public CommonSpider(CommonPageProcessor pageProcessor) {
        super(pageProcessor);
    }

    public long getProcessPageCount(){
        return ((CommonPageProcessor)this.pageProcessor).getProcessPageCount();
    }

    public long getPipelinePageCount(){
        return ((CommonPageProcessor)this.pageProcessor).getPipelinePageCount();
    }
}
