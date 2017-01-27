package model;

import java.util.List;

/**
 * Created by mian on 2017/1/26.
 * 链式表达式实体类，一个链式表达式包含多个表达式
 */
public class LinksExtractRule {
    //名称
    private String name;

    //表达式列表
    private List<Expression> expressions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }
}
