package Observer.Subject;

import Observer.Observer.Observer;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Administrator on 2016/10/8.
 */
public abstract class AbstractSubject implements Subject {
    private Vector<Observer> vector = new Vector<Observer>();

    @Override
    public void add(Observer observer) {
        vector.add(observer);
    }

    @Override
    public void remove(Observer observer) {
        vector.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Enumeration<Observer> observers = vector.elements();
        while (observers.hasMoreElements()){
            observers.nextElement().update();
        }
    }
}
