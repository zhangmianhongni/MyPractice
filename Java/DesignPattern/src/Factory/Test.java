package Factory;

import Factory.Car.Car;
import Factory.Factory.Factory;

/**
 * Created by Administrator on 2016/10/7.
 */
public class Test {
    public static void main(String[] args){
        Factory factory = new Factory();
        Car benz = factory.getCarInstance("Benz");
        benz.run();
        benz.stop();

        Car ford = factory.getCarInstance("Ford");
        ford.run();
        ford.stop();
    }
}
