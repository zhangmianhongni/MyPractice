package Observer;

import Observer.Observer.Observer;
import Observer.Observer.Observer1;
import Observer.Observer.Observer2;
import Observer.Subject.MySubject;
import Observer.Subject.Subject;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Test {
    public static void main(String[] args) {
        Observer observer1 = new Observer1();
        Observer observer2 = new Observer2();
        Subject mySubject = new MySubject();
        mySubject.add(observer1);
        mySubject.add(observer2);
        mySubject.operation();
    }
}
