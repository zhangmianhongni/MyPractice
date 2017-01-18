package FactoryMethod;

import FactoryMethod.Factory.BroomFactory;
import FactoryMethod.Factory.Factory;
import FactoryMethod.Factory.PlaneFactory;
import FactoryMethod.Moveable.Moveable;

/**
 * Created by Administrator on 2016/10/7.
 */
public class Test {
    public static void main(String[] args) {
        Factory planeFactory = new PlaneFactory();
        Moveable plane = planeFactory.create();
        plane.run();

        Factory broomFactory = new BroomFactory();
        Moveable broom = broomFactory.create();
        broom.run();
    }
}
