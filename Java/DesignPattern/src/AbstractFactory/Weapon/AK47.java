package AbstractFactory.Weapon;

/**
 * Created by Administrator on 2016/10/7.
 */
public class AK47 implements Weapon {
    @Override
    public void shoot() {
        System.out.println("AK47开枪。。。。。");
    }
}
