package antratraining.week2.aop;

import antratraining.week2.aop.annotation.AfterReturn;
import antratraining.week2.aop.annotation.AfterThrow;
import antratraining.week2.aop.annotation.Around;
import antratraining.week2.aop.interceptor.AfterReturnMethodInterceptor;
import antratraining.week2.aop.interceptor.AfterThrowMethodInterceptor;
import antratraining.week2.aop.interceptor.AroundMethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JDKReflectiveInvocationHandler implements InvocationHandler{

    private Object realInstance;
    private Object aspectInstance;

    public JDKReflectiveInvocationHandler(Object realInstance, Object aspectInstance) {
        this.realInstance = realInstance;
        this.aspectInstance = aspectInstance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> clazz = aspectInstance.getClass();
        List<MethodInterceptor> methodInterceptorList = new ArrayList<>();
        for(Method aspectMethod: clazz.getDeclaredMethods()) {
            for(Annotation annotation: aspectMethod.getDeclaredAnnotations()) {
                if(annotation.annotationType() == AfterReturn.class) {
                    methodInterceptorList.add(new AfterReturnMethodInterceptor(aspectInstance, aspectMethod));
                } else if(annotation.annotationType() == AfterThrow.class) {
                    methodInterceptorList.add(new AfterThrowMethodInterceptor(aspectInstance, aspectMethod));
                }
                else if(annotation.annotationType() == Around.class) {
                    methodInterceptorList.add(new AroundMethodInterceptor(aspectInstance, aspectMethod));
                }
            }
        }
        ProxyMethodInvocation proxyMethodInvocation = new ProxyMethodInvocation(
                methodInterceptorList,
                realInstance,
                method
        );

        //This is the point where I'm supposed to handle the exception
        //In our case, the return value will remain unused anyway so I just return null
        //if there is an exception caught.
        //in real world application we might have to account more carefully for the return value
        //in case of different kinds of exception

        Object resval = null;
        try{

            resval = proxyMethodInvocation.proceed();

        }
        catch(Exception e){

            return resval;

        }
        return resval;
    }


}
