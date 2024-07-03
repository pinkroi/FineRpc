package com.sf.fine.rpc.annotation;

import com.sf.fine.rpc.provider.RpcProviderAnnotationRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RpcProviderAnnotationRegister.class)
public @interface RpcProvider {

    Class<?> serviceInterface() default Object.class;

    String serviceVersion() default "1.0.0";

}
