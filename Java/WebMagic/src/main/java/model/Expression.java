package model;

/**
 * Created by mian on 2017/1/23.
 * 表达式实体类
 */
public class Expression {
    //表达式类型，默认XPath
    private ExpressionType expressionType = ExpressionType.XPath;
    //表达式参数
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

    public int getArgumentCount(){
        int count = 0;
        if(this.arguments != null){
            count = this.arguments.length;
        }
        return count;
    }
}
