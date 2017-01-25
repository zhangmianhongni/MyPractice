package model;

/**
 * Created by Administrator on 2017/1/23.
 */
public class Expression {
    private ExpressionType expressionType = ExpressionType.XPath;
    private Object[] arguments;

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
