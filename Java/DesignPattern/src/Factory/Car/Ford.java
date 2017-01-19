package Factory.Car;

/**
 * Created by Administrator on 2016/10/7.
 */
public class Ford implements Car {
    @Override
    public void run() {
        System.out.println("Ford开始启动了。。。");
    }

    @Override
    public void stop() {
        System.out.println("Ford停车了。。。。");
    }
}
