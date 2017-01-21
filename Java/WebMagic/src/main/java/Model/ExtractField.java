package model;

import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.Selector;
import static us.codecraft.webmagic.selector.Selectors.*;

/**
 * Created by mian on 2017/1/12.
 * 抽取字段实体类
 */
public class ExtractField {
    //字段名称
    private String fieldName;

    //抽取字段来源，默认HTML内容
    private FieldSourceType fieldSourceType = FieldSourceType.Html;

    //是否必须字段，默认False
    private boolean isNeed = false;

    //是否多个
    private boolean multi = false;

    //字段抽取表达式，默认XPath
    private ExpressionType expressionType = ExpressionType.XPath;

    private String expressionValue;

    //表达式参数
    private String[] expressionParams;

    private volatile Selector selector;




    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldSourceType getFieldSourceType() {
        return fieldSourceType;
    }

    public void setFieldSourceType(FieldSourceType fieldSourceType) {
        this.fieldSourceType = fieldSourceType;
    }

    public boolean isNeed() {
        return isNeed;
    }

    public void setNeed(boolean isNeed) {
        this.isNeed = isNeed;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public String getExpressionValue() {
        return expressionValue;
    }

    public void setExpressionValue(String expressionValue) {
        this.expressionValue = expressionValue;
    }

    public String[] getExpressionParams() {
        return expressionParams;
    }

    public void setExpressionParams(String[] expressionParams) {
        this.expressionParams = expressionParams;
    }

    public Selector getSelector() {
        if (selector == null) {
            synchronized (this) {
                if (selector == null) {
                    selector = compileSelector();
                }
            }
        }
        return selector;
    }

    private Selector compileSelector() {
        switch (expressionType) {
            case Css:
                if (expressionParams != null && expressionParams.length >= 1) {
                    return $(expressionValue, expressionParams[0]);
                } else {
                    return $(expressionValue);
                }
            case XPath:
                return xpath(expressionValue);
            case Regex:
                if (expressionParams != null && expressionParams.length >= 1) {
                    return regex(expressionValue, Integer.parseInt(expressionParams[0]));
                } else {
                    return regex(expressionValue);
                }
            case JsonPath:
                return new JsonPathSelector(expressionValue);
            default:
                return xpath(expressionValue);
        }
    }
}



