package Factory.Factory;

import Factory.Car.Benz;
import Factory.Car.Car;
import Factory.Car.Ford;

/**
 * Created by Administrator on 2016/10/7.
 */
public class Factory {
    public Car getCarInstance(String type){
        Car c = null;
        if ("Benz".equals(type)){
            c = new Benz();
        }
        if ("Ford".equals(type)){
            c = new Ford();
        }

        return c;
    }
}
