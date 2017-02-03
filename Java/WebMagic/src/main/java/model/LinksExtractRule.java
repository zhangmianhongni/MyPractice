package model;

import constant.ExtractType;
import org.apache.http.NameValuePair;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/26.
 * 链接抽取规则实体类，抽取方法分为网页和API
 * 网页使用链式表达式一个链式表达式包含多个表达式
 * API使用分页规则
 */
public class LinksExtractRule {
    //名称
    private String name;

    //抽取方式
    private ExtractType extractType = ExtractType.Page;

    //表达式列表
    private List<Expression> expressions;

    //请求方法
    private String requestMethod = HttpConstant.Method.GET;

    //请求参数
    private NameValuePair[] requestParams;

    //请求参数附加值，可选，对应请求参数
    //如果是整数，每次请求额外加法
    private Map<String, String> requestParamsExtra;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExtractType getExtractType() {
        return extractType;
    }

    public void setExtractType(ExtractType extractType) {
        this.extractType = extractType;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public NameValuePair[] getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(NameValuePair[] requestParams) {
        this.requestParams = requestParams;
    }

    public Map<String, String> getRequestParamsExtra() {
        return requestParamsExtra;
    }

    public void setRequestParamsExtra(Map<String, String> requestParamsExtra) {
        this.requestParamsExtra = requestParamsExtra;
    }
}
