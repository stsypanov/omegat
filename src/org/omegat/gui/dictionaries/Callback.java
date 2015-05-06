package org.omegat.gui.dictionaries;

/**
 * Created by Сергей on 25.04.2015.
 */
public interface Callback<T> {

    void execute(T arg);
}
