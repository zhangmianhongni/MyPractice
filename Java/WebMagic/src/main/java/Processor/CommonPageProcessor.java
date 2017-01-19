package Processor;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mian on 2017/1/12.
 */
public class CommonPageProcessor implements PageProcessor {
    private Site site;

    private String targetRequesCssSelector;
    private String targetRequesRegex;

    public CommonPageProcessor(){
        this.site = Site.me();
    }

    public void process(Page page) {
        List<String> urls = this.initTargetRequests(page);
        page.addTargetRequests(urls);
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }

    public Site getSite() {
        return this.site;
    }

    private List<String> initTargetRequests(Page page){
        List<String> urls = null;
        if(StringUtils.isNotEmpty(targetRequesCssSelector) && StringUtils.isNotEmpty(targetRequesRegex)){
            urls = page.getHtml().css(targetRequesCssSelector).links().regex(targetRequesRegex).all();
        }

        if(StringUtils.isNotEmpty(targetRequesCssSelector)){
            urls = page.getHtml().css(targetRequesCssSelector).links().all();
        }

        if(StringUtils.isNotEmpty(targetRequesRegex)){
            urls = page.getHtml().links().regex(targetRequesRegex).all();
        }

        return urls;
    }

    public CommonPageProcessor setTargetRequests(String cssSelector, String urlRegex){
        this.targetRequesCssSelector = cssSelector;
        this.targetRequesRegex = urlRegex;
        return this;
    }

    public CommonPageProcessor setExtractFields(){
        return this;
    }



    /*Site属性*/

    public CommonPageProcessor setCookies(Map<String, String> cookies){
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            this.site.addCookie(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public CommonPageProcessor setUserAgent(String userAgent) {
        this.site.setUserAgent(userAgent);
        return this;
    }


    public CommonPageProcessor setDomain(String domain) {
        this.site.setDomain(domain);
        return this;
    }

    public CommonPageProcessor setCharset(String charset) {
        this.site.setDomain(charset);
        return this;
    }

    public CommonPageProcessor setTimeOut(int timeOut) {
        this.site.setTimeOut(timeOut);
        return this;
    }

    public CommonPageProcessor setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.site.setAcceptStatCode(acceptStatCode);
        return this;
    }

    public CommonPageProcessor setSleepTime(int sleepTime) {
        this.site.setSleepTime(sleepTime);
        return this;
    }

    public CommonPageProcessor setRetryTimes(int retryTimes) {
        this.site.setRetryTimes(retryTimes);
        return this;
    }

    public CommonPageProcessor setHeaders(Map<String, String> headers){
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.site.addHeader(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public CommonPageProcessor setCycleRetryTimes(int cycleRetryTimes) {
        this.site.setCycleRetryTimes(cycleRetryTimes);
        return this;
    }

    public CommonPageProcessor setHttpProxy(HttpHost httpProxy) {
        this.site.setHttpProxy(httpProxy);
        return this;
    }

    public CommonPageProcessor setUsernamePasswordCredentials(UsernamePasswordCredentials usernamePasswordCredentials) {
        this.site.setUsernamePasswordCredentials(usernamePasswordCredentials);
        return this;
    }

    public CommonPageProcessor setHttpProxyPool(ProxyPool proxyPool) {
        this.site.setHttpProxyPool(proxyPool);
        return this;
    }

    public CommonPageProcessor setHttpProxyPool(List<String[]> httpProxyList, boolean isUseLastProxy) {
        this.site.setHttpProxyPool(httpProxyList, isUseLastProxy);
        return this;
    }

    public CommonPageProcessor setUseGzip(boolean useGzip) {
        this.site.setUseGzip(useGzip);
        return this;
    }

    public CommonPageProcessor setRetrySleepTime(int retrySleepTime) {
        this.site.setRetrySleepTime(retrySleepTime);
        return this;
    }
}
