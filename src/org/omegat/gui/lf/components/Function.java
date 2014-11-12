package org.omegat.gui.lf.components;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:21
 */
@SuppressWarnings({"unchecked"})
public interface Function<Param, Result> {
    Result fun(Param param);

    Function ID = new Function() {
        @Override
        public Object fun(final Object o) {
            return o;
        }
    };

    Function NULL = NullableFunction.NULL;

    Function TO_STRING = new Function() {
        @Override
        public Object fun(Object o) {
            return String.valueOf(o);
        }
    };

    final class Self<P, R> implements Function<P, R> {
        @Override
        public R fun(P p) {
            return (R)p;
        }
    }

    interface Mono<T> extends Function<T, T> {}

        final class InstanceOf<P, R extends P> implements NullableFunction<P, R> {

        private final Class<R> myResultClass;

        public InstanceOf(Class<R> resultClass) {
            myResultClass = resultClass;
        }

        @Override
        @Nullable
        public R fun(P p) {
            return p.getClass().isAssignableFrom(myResultClass) ? (R)p : null;
        }
    }

    final class First<P, R extends P> implements Function<P[], R> {
        @Override
        public R fun(P[] ps) {
            return (R)ps[0];
        }
    }

    final class FirstInCollection<P, R extends P> implements Function<Collection<P>, R> {
        @Override
        public R fun(Collection<P> ps) {
            return (R)ps.iterator().next();
        }
    }

    class Predefined {
        public static <I,O> Function<I, O> NULL() {
            return NULL;
        }
        public static <I,O> Function<I, O> TO_STRING() {
            return TO_STRING;
        }
    }
}
