package org.omegat.gui.lf.components;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:19
 */
public class Pair<A, B> {
    public final A first;
    public final B second;

    @NotNull
    public static <A, B> Pair<A, B> create(A first, B second) {
        //noinspection DontUsePairConstructor
        return new Pair<>(first, second);
    }

    @NotNull
    public static <A, B> Function<A, Pair<A, B>> createFunction(final B value) {
        return new Function<A, Pair<A, B>>() {
            @Override
            public Pair<A, B> fun(A a) {
                return create(a, value);
            }
        };
    }

    public static <T> T getFirst(Pair<T, ?> pair) {
        return pair != null ? pair.first : null;
    }

    public static <T> T getSecond(Pair<?, T> pair) {
        return pair != null ? pair.second : null;
    }

    @SuppressWarnings("unchecked")
    private static final Pair EMPTY = create(null, null);

    @SuppressWarnings("unchecked")
    public static <A, B> Pair<A, B> empty() {
        return EMPTY;
    }

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public final A getFirst() {
        return first;
    }

    public final B getSecond() {
        return second;
    }

    public final boolean equals(Object o) {
        return o instanceof Pair && Comparing.equal(first, ((Pair)o).first) && Comparing.equal(second, ((Pair)o).second);
    }

    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "<" + first + "," + second + ">";
    }
}
