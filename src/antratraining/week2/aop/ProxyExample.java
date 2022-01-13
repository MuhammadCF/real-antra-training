package antratraining.week2.aop;

import antratraining.week2.aop.annotation.AfterReturn;
import antratraining.week2.aop.annotation.AfterThrow;
import antratraining.week2.aop.annotation.Around;

import antratraining.week2.aop.MethodInvocation;

//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyExample {

    public static void main(String[] args) throws Exception{
        Calculator c1 = new CalculatorImpl1();
        Calculator proxy = (Calculator) Proxy.newProxyInstance(
                c1.getClass().getClassLoader(),
                new Class[]{Calculator.class},
                new JDKReflectiveInvocationHandler(c1, new CalculatorAspect())
        );

        //NOTE:
        //I recommend running each method below one by one for clearer result
        //(but running them all at once doesn't hurt)
        proxy.get();
        proxy.run();

    }


}


//class MyInvocationHandler implements InvocationHandler {
//    private Object realInstance;
//
//    public MyInvocationHandler(Object realInstance) {
//        this.realInstance = realInstance;
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("this is our proxy");
//        return method.invoke(realInstance);
//    }
//}

interface Calculator {
    void run();
    void get();
}

class CalculatorImpl1 implements Calculator {
    @Override
    public void run() {


        System.out.println("this is our calculator run (testing afterThrow)");
        //This is where I start the testing by throwing an arbitrary exception to hopefully
        //later trigger the afterThrow aspects (and not trigger the afterReturn aspects).
        throw new RuntimeException();

    }

    @Override
    public void get() {

        //this will be used to test the afterReturn aspects.
        //note the lack of exception thrown
        System.out.println("this is our calculator get (testing afterReturn)");
    }

}

class CalculatorAspect {

    @AfterReturn
    public void afterLogic1() {
        System.out.println("this is after return-- number 1");
    }

    @AfterThrow
    public void afterLogic2() {
        System.out.println("this is after throw -- number 1");
    }

    @AfterReturn
    public void afterLogic3() {
        System.out.println("this is after return -- number 2");
    }

    @AfterThrow
    public void afterLogic4() {
        System.out.println("this is after throw -- number 2");
    }

    @Around
    public Object aroundLogic1(MethodInvocation methodInvocation) throws Exception{
        System.out.println("this is around throw (before part)");


        //I don't know what is the correct behavior to handle exceptions for Around aspects.
        //do we skip the after part in case of exception or do we print it anyway?
        //I opted for the former so if exception occurred the last print statement will not be executed

        Object res = methodInvocation.proceed();
        System.out.println("this is around throw (after part)");

        return res;
    }

}
