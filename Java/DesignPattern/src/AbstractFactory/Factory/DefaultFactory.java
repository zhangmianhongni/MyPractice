package AbstractFactory.Factory;

import AbstractFactory.Food.Apple;
import AbstractFactory.Food.Food;
import AbstractFactory.Weapon.AK47;
import AbstractFactory.Weapon.Weapon;

/**
 * Created by Administrator on 2016/10/7.
 */
public class DefaultFactory extends AbstractFactory {
    @Override
    public Food createFood() {
        return new Apple();
    }

    @Override
    public Weapon createWeapon() {
        return new AK47();
    }
}
