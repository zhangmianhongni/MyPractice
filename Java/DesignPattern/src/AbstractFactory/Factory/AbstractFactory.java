package AbstractFactory.Factory;

import AbstractFactory.Food.Food;
import AbstractFactory.Weapon.Weapon;

/**
 * Created by Administrator on 2016/10/7.
 */
public abstract class AbstractFactory {
    public abstract Food createFood();
    public abstract Weapon createWeapon();
}
