package org.omegat.gui.lf.components;

import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:21
 */
public class Ref<T> {
    private T myValue;

    public Ref() { }

    public Ref(@Nullable T value) {
        myValue = value;
    }

    public boolean isNull() {
        return myValue == null;
    }

    public T get() {
        return myValue;
    }

    public void set(@Nullable T value) {
        myValue = value;
    }

    public boolean setIfNull(@Nullable T value) {
        if (myValue == null) {
            myValue = value;
            return true;
        }
        return false;
    }

    public static <T> Ref<T> create() {
        return new Ref<>();
    }

    public static <T> Ref<T> create(@Nullable T value) {
        return new Ref<>(value);
    }

    @Override
    public String toString() {
        return String.valueOf(myValue);
    }
}
