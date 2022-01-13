package antratraining.week2.aop.interceptor;

import antratraining.week2.aop.MethodInterceptor;
import antratraining.week2.aop.MethodInvocation;

import java.lang.reflect.Method;

public class AfterReturnMethodInterceptor implements MethodInterceptor {
    private Object aspectInstance;
    private Method aspectMethod;

    public AfterReturnMethodInterceptor(Object aspectInstance, Method aspectMethod) {
        this.aspectInstance = aspectInstance;
        this.aspectMethod = aspectMethod;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Exception {
        aspectMethod.setAccessible(true);
        Object res = null;

        //if there is no exception then return the expected result, otherwise rethrow the error.
        //again as I've said in the README, this constant rethrowing up the stack
        //might muddle the call trace, making it harder to troubleshoot
        //the error. But we have to somehow carry rhe exception all the way out of the consecutive
        //interceptors' invocation to JDKReflectiveInvocationHandler to be processed there.
        try{

            res = methodInvocation.proceed();

        }
        catch(Exception e){

            throw e;

        }

        aspectMethod.invoke(aspectInstance);
        return res;
    }
}
