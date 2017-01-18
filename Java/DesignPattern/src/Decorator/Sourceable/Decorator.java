package Decorator.Sourceable;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Decorator implements Sourceable {
    private Sourceable source;

    public Decorator(Sourceable source){
        super();
        this.source = source;
    }

    @Override
    public void method() {
        System.out.println("before decorator!");
        this.source.method();
        System.out.println("after decorator!");
    }
}
