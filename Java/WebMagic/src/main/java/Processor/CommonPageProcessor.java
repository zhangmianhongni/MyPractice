package processor;

import model.Expression;
import model.ExtractField;
import model.ExpressionType;
import model.FieldSourceType;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyPool;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/12.
 */
public abstract class CommonPageProcessor implements PageProcessor {
    Site site;

    String targetRequestCssSelector;
    String targetRequestRegex;
    String pagedRequestCssSelector;
    String pagedRequestRegex;
    List<ExtractField> extractFields;
    List<Expression> expressions;

    public Site getSite() {
        return this.site;
    }



    public CommonPageProcessor setTargetRequestsUrl(String cssSelector, String urlRegex){
        this.targetRequestCssSelector = cssSelector;
        this.targetRequestRegex = urlRegex;
        return this;
    }

    public CommonPageProcessor setPagedRequestsUrl(String cssSelector, String urlRegex){
        this.pagedRequestCssSelector = cssSelector;
        this.pagedRequestRegex = urlRegex;
        return this;
    }

    public CommonPageProcessor setTargetRequestExpressions(List<Expression> expressions){
        this.expressions = expressions;
        return this;
    }

    public CommonPageProcessor setExtractFields(List<ExtractField> extractFields){
        this.extractFields = extractFields;
        return this;
    }


    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等 Site属性

    //设置Cookies
    public CommonPageProcessor setCookies(Map<String, String> cookies){
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            this.site.addCookie(entry.getKey(), entry.getValue());
        }
        return this;
    }

    //设置UserAgent
    public CommonPageProcessor setUserAgent(String userAgent) {
        this.site.setUserAgent(userAgent);
        return this;
    }


    public CommonPageProcessor setDomain(String domain) {
        this.site.setDomain(domain);
        return this;
    }

    //设置编码
    public CommonPageProcessor setCharset(String charset) {
        this.site.setDomain(charset);
        return this;
    }

    //设置超时时间，单位是毫秒
    public CommonPageProcessor setTimeOut(int timeOut) {
        this.site.setTimeOut(timeOut);
        return this;
    }

    //设置通过的状态编码
    public CommonPageProcessor setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.site.setAcceptStatCode(acceptStatCode);
        return this;
    }

    //设置延迟时间
    public CommonPageProcessor setSleepTime(int sleepTime) {
        this.site.setSleepTime(sleepTime);
        return this;
    }

    //设置重试次数
    public CommonPageProcessor setRetryTimes(int retryTimes) {
        this.site.setRetryTimes(retryTimes);
        return this;
    }

    //设置Headers
    public CommonPageProcessor setHeaders(Map<String, String> headers){
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.site.addHeader(entry.getKey(), entry.getValue());
        }
        return this;
    }

    //设置循环重试次数
    //循环重试cycleRetry是0.3.0版本加入的机制
    //该机制会将下载失败的url重新放入队列尾部重试，直到达到重试次数，以保证不因为某些网络原因漏抓页面。
    public CommonPageProcessor setCycleRetryTimes(int cycleRetryTimes) {
        this.site.setCycleRetryTimes(cycleRetryTimes);
        return this;
    }

    //设置Http代理
    public CommonPageProcessor setHttpProxy(HttpHost httpProxy) {
        this.site.setHttpProxy(httpProxy);
        return this;
    }

    //Http代理用户名、密码
    public CommonPageProcessor setUsernamePasswordCredentials(UsernamePasswordCredentials usernamePasswordCredentials) {
        this.site.setUsernamePasswordCredentials(usernamePasswordCredentials);
        return this;
    }

    //设置Http代理池
    public CommonPageProcessor setHttpProxyPool(ProxyPool proxyPool) {
        this.site.setHttpProxyPool(proxyPool);
        return this;
    }

    //设置Http代理池
    public CommonPageProcessor setHttpProxyPool(List<String[]> httpProxyList, boolean isUseLastProxy) {
        this.site.setHttpProxyPool(httpProxyList, isUseLastProxy);
        return this;
    }

    //设置是否使用Gzip压缩
    public CommonPageProcessor setUseGzip(boolean useGzip) {
        this.site.setUseGzip(useGzip);
        return this;
    }

    //设置重试延迟时间
    public CommonPageProcessor setRetrySleepTime(int retrySleepTime) {
        this.site.setRetrySleepTime(retrySleepTime);
        return this;
    }
}
