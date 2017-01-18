package FactoryMethod.Factory;

import FactoryMethod.Moveable.Broom;
import FactoryMethod.Moveable.Moveable;

/**
 * Created by Administrator on 2016/10/7.
 */
public class BroomFactory extends Factory {
    @Override
    public Moveable create() {
        return new Broom();
    }
}
