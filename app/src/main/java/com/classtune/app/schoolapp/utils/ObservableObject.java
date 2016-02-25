package com.classtune.app.schoolapp.utils;

import java.util.Observable;

/**
 * Created by BLACK HAT on 25-Feb-16.
 */
public class ObservableObject extends Observable {
    private static ObservableObject instance = new ObservableObject();

    public static ObservableObject getInstance() {
        return instance;
    }

    private ObservableObject() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}
