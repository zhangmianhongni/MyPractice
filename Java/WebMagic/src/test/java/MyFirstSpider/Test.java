package MyFirstSpider;

import Processor.CommonPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * Created by mian on 2017/1/12.
 */
public class Test {
    public static void main(String[] args) {
        //Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/zhangmianhongni").thread(5).run();

        CommonPageProcessor processor = new CommonPageProcessor();
        processor.setRetryTimes(3);
        processor.setSleepTime(100);
        processor.setTimeOut(10000);
        processor.setTargetRequests("div.js-pinned-repos-reorder-container", "(https://github\\.com/zhangmianhongni/\\w+)");
        Spider.create(processor).addUrl("https://github.com/zhangmianhongni").thread(5).run();
    }
}
