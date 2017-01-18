package AbstractFactory.Factory;

import AbstractFactory.Food.Food;
import AbstractFactory.Food.Orange;
import AbstractFactory.Weapon.M4;
import AbstractFactory.Weapon.Weapon;

/**
 * Created by Administrator on 2016/10/7.
 */
public class CustomFactory extends AbstractFactory {
    @Override
    public Food createFood() {
        return new Orange();
    }

    @Override
    public Weapon createWeapon() {
        return new M4();
    }
}
