package model;

public enum ExpressionType {
    XPath("xpath"),
    Css("css"),
    Regex("regex"),
    JsonPath("jsonPath"),
    Links("links");

    private String methodName;

    ExpressionType(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }
}
