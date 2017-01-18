package AbstractFactory;

import AbstractFactory.Factory.AbstractFactory;
import AbstractFactory.Factory.DefaultFactory;
import AbstractFactory.Factory.CustomFactory;
import AbstractFactory.Weapon.Weapon;
import AbstractFactory.Food.Food;

/**
 * Created by Administrator on 2016/10/7.
 */
public class Test {
    public static void main(String[] args){
        AbstractFactory f = new DefaultFactory();
        Weapon w = f.createWeapon();
        w.shoot();
        Food a = f.createFood();
        a.getName();

        AbstractFactory ff = new CustomFactory();
        Weapon ww = ff.createWeapon();
        ww.shoot();
        Food aa = ff.createFood();
        aa.getName();
    }
}
