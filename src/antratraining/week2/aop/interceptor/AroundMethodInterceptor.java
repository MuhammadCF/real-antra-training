package antratraining.week2.aop.interceptor;

import antratraining.week2.aop.MethodInterceptor;
import antratraining.week2.aop.MethodInvocation;

import java.lang.reflect.Method;

public class AroundMethodInterceptor implements MethodInterceptor {
    private Object aspectInstance;
    private Method aspectMethod;

    public AroundMethodInterceptor(Object aspectInstance, Method aspectMethod) {
        this.aspectInstance = aspectInstance;
        this.aspectMethod = aspectMethod;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Exception {
        aspectMethod.setAccessible(true);
        Object res = methodInvocation.proceed();
        aspectMethod.invoke(aspectInstance);
        return res;
    }
}
