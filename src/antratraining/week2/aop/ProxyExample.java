package antratraining.week2.aop;

import antratraining.week2.aop.annotation.AfterReturn;
import antratraining.week2.aop.annotation.AfterThrow;

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


        System.out.println("this is our calculator run (testing afterThrow");
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
}
