package Decorator;

import Decorator.Sourceable.Decorator;
import Decorator.Sourceable.Sourceable;
import Decorator.Sourceable.Source;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Test {
    public static void main(String[] args) {
        Sourceable source = new Source();
        Sourceable obj = new Decorator(source);
        obj.method();
    }
}
