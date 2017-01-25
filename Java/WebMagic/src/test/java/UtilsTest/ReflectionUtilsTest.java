package UtilsTest;

import us.codecraft.webmagic.selector.Html;
import utils.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/1/26.
 */
public class ReflectionUtilsTest {
    public static void main(String[] args) throws NoSuchMethodException {
        Method method = ReflectionUtils.findMethodWithSuperClass(Html.class, "css", 2);
        System.out.println(method);
    }
}
