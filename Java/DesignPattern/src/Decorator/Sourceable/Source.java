package Decorator.Sourceable;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Source implements Sourceable {
    @Override
    public void method() {
        System.out.println("the original method!");
    }
}
