package FactoryMethod.Factory;

import FactoryMethod.Moveable.Moveable;
import FactoryMethod.Moveable.Plane;

/**
 * Created by Administrator on 2016/10/7.
 */
public class PlaneFactory extends Factory {
    @Override
    public Moveable create() {
        return new Plane();
    }
}
