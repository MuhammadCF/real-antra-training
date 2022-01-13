package antratraining.week2.aop.interceptor;

import antratraining.week2.aop.MethodInterceptor;
import antratraining.week2.aop.MethodInvocation;

import java.lang.reflect.Method;

public class AfterThrowMethodInterceptor implements MethodInterceptor {
    private Object aspectInstance;
    private Method aspectMethod;

    public AfterThrowMethodInterceptor(Object aspectInstance, Method aspectMethod) {
        this.aspectInstance = aspectInstance;
        this.aspectMethod = aspectMethod;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Exception {
        aspectMethod.setAccessible(true);


        Object res = null;

        //same as AfterReturn except now I invoke the aspectMethod only if I enter the catch
        try{

            res = methodInvocation.proceed();

        }
        catch(Exception e){

            aspectMethod.invoke(aspectInstance);
            throw e;

        }



        return res;
    }
}

