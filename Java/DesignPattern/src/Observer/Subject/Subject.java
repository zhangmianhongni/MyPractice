package Observer.Subject;

import Observer.Observer.Observer;

/**
 * Created by Administrator on 2016/10/8.
 */
public interface Subject {
    void add(Observer observer);

    void remove(Observer observer);

    void notifyObservers();

    void operation();
}
