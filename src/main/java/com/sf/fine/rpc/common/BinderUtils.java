package com.sf.fine.rpc.common;

import java.lang.reflect.Method;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

public class BinderUtils {

    private static boolean existBinder = false;

    static {
        try {
            Class.forName("org.springframework.boot.context.properties.bind.Binder");
            existBinder = true;
        } catch (ClassNotFoundException e) {
        }
    }

    public static <T> T bind(ConfigurableEnvironment environment, String prefix, Class<T> type) {
        if (existBinder) {
            return binderBind(environment, prefix, type);
        } else {
            throw new IllegalStateException(
                    "Can not find class org.springframework.boot.context.properties.bind.Binder");
        }
    }

    private static Method Binder_get_Method;
    private static Method Binder_bind_Method;

    private static Method Bindable_of_Method;

    private static Method BindResult_orElse_Method;

    @SuppressWarnings("unchecked")
    private static <T> T binderBind(ConfigurableEnvironment environment, String prefix, Class<T> type) {
        try {
            if (Binder_get_Method == null) {
                Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
                Class<?> bindableClass = Class.forName("org.springframework.boot.context.properties.bind.Bindable");
                Binder_get_Method = binderClass.getMethod("get", Environment.class);
                Binder_bind_Method = binderClass.getMethod("bind", String.class, bindableClass);
            }
            if (Bindable_of_Method == null) {
                Bindable_of_Method = Class.forName("org.springframework.boot.context.properties.bind.Bindable")
                        .getMethod("of", Class.class);
            }

            if (BindResult_orElse_Method == null) {
                BindResult_orElse_Method = Class.forName("org.springframework.boot.context.properties.bind.BindResult")
                        .getMethod("orElse", Object.class);
            }

            /**
             * <pre>
             * Binder binder = Binder.get(environment);
             * Bindable<FooProperties> bindable = Bindable.of(FooProperties.class);
             * BindResult<FooProperties> bindResult = binder.bind("foo", bindable);
             * FooProperties fooProperties = bindResult.get();
             * </pre>
             */
            Object binder = Binder_get_Method.invoke(null, environment);
            Object bindable = Bindable_of_Method.invoke(null, type);
            Object bindResult = Binder_bind_Method.invoke(binder, prefix, bindable);
            return (T) BindResult_orElse_Method.invoke(bindResult, type.newInstance());

        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

}
