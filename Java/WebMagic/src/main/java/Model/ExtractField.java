package model;

/**
 * Created by mian on 2017/1/12.
 * 抽取字段实体类
 */
public class ExtractField {
    //字段名称
    private String fieldName;

    //抽取字段来源，默认HTML内容
    private FieldSourceType fieldSourceType = FieldSourceType.HTML;

    //是否必须字段，默认False
    private boolean isNeed = false;

    //字段抽取类型，默认XPATH
    private FieldExtractType fieldExtractType = FieldExtractType.XPATH;

    //xpath
    private String xPath;

    //css选择器
    private String cssSelector;

    //css选择器-属性，可选
    private String cssSelectorAttr;

    //正则表达式
    private String regex;

    //正则表达式捕获组，可选
    private int regexGroup;






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

    public FieldExtractType getFieldExtractType() {
        return fieldExtractType;
    }

    public void setFieldExtractType(FieldExtractType fieldExtractType) {
        this.fieldExtractType = fieldExtractType;
    }

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getCssSelectorAttr() {
        return cssSelectorAttr;
    }

    public void setCssSelectorAttr(String cssSelectorAttr) {
        this.cssSelectorAttr = cssSelectorAttr;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getRegexGroup() {
        return regexGroup;
    }

    public void setRegexGroup(int regexGroup) {
        this.regexGroup = regexGroup;
    }
}


