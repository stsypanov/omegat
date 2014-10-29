package org.omegat.gui.lf.components;

import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:21
 */
public interface NullableFunction<Param, Result> extends Function<Param, Result> {
    @Override
    @Nullable
    Result fun(final Param param);

    /**
     * @see FunctionUtil#nullConstant()
     */
    NullableFunction NULL = new NullableFunction() {
        @Override
        public Object fun(final Object o) {
            return null;
        }
    };
}
