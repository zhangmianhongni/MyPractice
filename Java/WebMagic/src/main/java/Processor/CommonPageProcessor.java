package processor;

import constant.ExtractType;
import dao.MysqlDao;
import model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.ProxyPool;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;
import utils.ExtractUtils;
import utils.ReflectionUtils;
import utils.ResultUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 通用页面抽取处理类
 * PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现
 * Created by mian on 2017/1/12.
 */
public abstract class CommonPageProcessor implements PageProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    final AtomicLong processPageCount = new AtomicLong(0);

    Site site;
    List<ExtractField> extractFields;
    private List<LinksExtractRule> linksExtractRules;


    public CommonPageProcessor(){
        this.site = Site.me();
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public void process(Page page) {
        this.logger.debug("请求深度 {}", this.getCurDepth(page));
        this.logger.debug("请求链接 {}", page.getUrl().toString());
        this.doProcess(page);
        processPageCount.incrementAndGet();
    }

    abstract void doProcess(Page page);

    //部分二：从页面发现后续的url地址来抓取，给子类在process调用
    void extractTargetLinks(Page page){
        List<Request> targetRequests = new ArrayList<>();
        if(this.linksExtractRules != null){
            for (LinksExtractRule rule : linksExtractRules) {
                if(rule.getExtractType() == ExtractType.Page) {
                    List<Request> expressionRequests = this.extractTargetLinksByExp(page, rule);
                    if (expressionRequests != null) {
                        targetRequests.addAll(expressionRequests);

                        //todo 临时测试
                        if (rule.getName().equals("detail")) {
                            expressionRequests.stream().forEach(targetRequest -> MysqlDao.insertList(page.getUrl().toString(), targetRequest.getUrl()));
                        }
                    }
                }else if(rule.getExtractType() == ExtractType.Api){
                    targetRequests.add(this.getRequest(page.getUrl().toString(), page, rule));
                }
            }
            targetRequests.stream().forEach(page::addTargetRequest);
        }
    }

    private List<Request> extractTargetLinksByExp(Page page, LinksExtractRule rule){
        List<Request> requests = new ArrayList<>();
        List<Expression> expressions = rule.getExpressions();

        if (expressions != null && !expressions.isEmpty()) {
            Selectable linksSelectable = page.getHtml();
            try {
                for (Expression expression : expressions) {
                    Method method = ReflectionUtils.findMethodWithSuperClass(linksSelectable.getClass(), expression.getExpressionType().getMethodName(), expression.getArgumentCount());
                    linksSelectable = (Selectable) method.invoke(linksSelectable, expression.getArguments());
                }
                linksSelectable.all().stream().forEach(url -> requests.add(this.getRequest(url, page, rule)));
            } catch (InvocationTargetException | IllegalAccessException e) {
                this.logger.warn("初始化页面Url出错", e);
            }
        }

        return requests;
    }

    private Request getRequest(String url, Page page, LinksExtractRule rule){
        Request request = new Request(url);
        request.putExtra(ExtractUtils.SOURCE_REQ_URL_STR, page.getUrl().toString());
        request.setMethod(rule.getRequestMethod());
        int curDepth = this.getCurDepth(page);
        if(rule.getRequestParams() != null && rule.getRequestParams().length > 0){
            NameValuePair[] nameValuePairs = ExtractUtils.getExtraRequestParams(rule.getRequestParams(), rule.getRequestParamsExtra(), curDepth);
            request.putExtra(ExtractUtils.REQUEST_PARAMS_STR, nameValuePairs);
            //因为GET方法WebMagic默认没添加参数，所以把参数拼在Url后面
            if(rule.getRequestMethod().equals(HttpConstant.Method.GET)){
                request.setUrl(ExtractUtils.getUrlByParamsMap(url, nameValuePairs));
            }
        }
        request.putExtra(ExtractUtils.REQUEST_DEPTH_STR, curDepth + 1);
        return request;
    }

    int getCurDepth(Page page){
        int depth = 1;
        Request request = page.getRequest();
        if(request.getExtra(ExtractUtils.REQUEST_DEPTH_STR) != null && StringUtils.isNumeric(request.getExtra(ExtractUtils.REQUEST_DEPTH_STR).toString())){
            depth = Integer.parseInt(request.getExtra(ExtractUtils.REQUEST_DEPTH_STR).toString());
        }

        return depth;
    }

    void addUrlField(Map<String, Object> fields, Page page){
        fields.put(ResultUtils.URL_STR, page.getRequest().getUrl());
    }

    //如果捉取的是列表页面，设置为true
    void setMultiItemsPage(Page page, boolean isMultiItems){
        page.putField(ResultUtils.IS_MULTI_ITEMS_STR, isMultiItems);
    }

    public long getProcessPageCount() {
        return processPageCount.get();
    }

    public long getPipelinePageCount(){
        return processPageCount.get();
    }


    public CommonPageProcessor setTargetRequestRules(List<LinksExtractRule> linksExtractRules){
        this.linksExtractRules = linksExtractRules;
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
