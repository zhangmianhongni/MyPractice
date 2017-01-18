package Observer.Observer;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Observer2 implements Observer {
    @Override
    public void update() {
        System.out.println("observer2 has received!");
    }
}
