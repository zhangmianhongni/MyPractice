package Observer.Subject;

/**
 * Created by Administrator on 2016/10/8.
 */
public class MySubject extends AbstractSubject {
    @Override
    public void operation() {
        System.out.println("update self!");
        notifyObservers();
    }
}
