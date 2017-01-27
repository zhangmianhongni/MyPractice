package utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mian on 2017/1/26.
 * 反射帮助类
 */
public class ReflectionUtils {
    private static Method findMethod(Method[] methods, String methodName,  int parameterCount) {
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterCount() == parameterCount) {
                return method;
            }
        }
        return null;
    }

    public static Method findMethodWithSuperClass(Class<?> clazz, String methodName, int parameterCount){
        Method method = findMethod(clazz.getMethods(), methodName, parameterCount);
        if(method != null){
            return method;
        }else{
            if(clazz.getSuperclass() != null) {
                return findMethodWithSuperClass(clazz.getSuperclass(), methodName, parameterCount);
            }else{
                return null;
            }
        }
    }
}
