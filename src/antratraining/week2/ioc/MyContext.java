package antratraining.week2.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

//I have re-purposed your code to support constructor injection

public class MyContext {
    private static final Map<String, Object> objMap = new HashMap<>();

    //in my implementation the objMap contains only the classes/types to use for injections later.
    //the classes that we will actually inject to are contained in different containers:
    //injectedMap and classListToBeInjected

    private static final Map<String, Object> injectedMap = new HashMap<>();

    private static List<Class<?>> classListToBeInjected = new ArrayList<>();

    private static List<Class<?>> scan() throws Exception {
        return Arrays.asList(EmployeeService1.class, EmployeeService2.class);
    }

    public static void init(List<Class<?>> injectee) throws Exception {

        //my implementation requires the caller to pass in a list of classes to be scanned and injected
        //with all the necessary instances from objMap
        //this is for modularity sake
        classListToBeInjected = injectee;
        List<Class<?>> classes = scan();
        for(Class<?> clazz: classes) {
            String name = clazz.getSimpleName();
            Object instance = clazz.getDeclaredConstructor().newInstance();
            objMap.put(name, instance);
        }
        for(Class<?> clazz: classListToBeInjected) {
            //Class<?> clazz = instance.getClass();
            for(Constructor constructor: clazz.getDeclaredConstructors()) {
                for(Annotation annotation: constructor.getDeclaredAnnotations()) {
                    if(annotation.annotationType() == Autowired.class) {
//                        String name = field.getType().getSimpleName();
//                        field.setAccessible(true);
//                        field.set(instance, objMap.get(name));
                        String className = clazz.getSimpleName();
                        Class<?>[] params = constructor.getParameterTypes();
                        Object[] instances = new Object[params.length];
                        for(int i = 0;i < params.length;i++){
                            String name = params[i].getSimpleName();
                            instances[i] = objMap.get(name);
                        }

                        injectedMap.put(className, constructor.newInstance(instances));

                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{

        init(Arrays.asList(TmpService.class));
        System.out.println(injectedMap);
    }

}

class TmpService {

    private EmployeeService1 ss1;


    private EmployeeService2 ss2;

    @Autowired
    public TmpService(EmployeeService1 ss1, EmployeeService2 ss2){
        this.ss1 = ss1;
        this.ss2 = ss2;


    }

    //I have declared two other unused constructors to highlight the fact that only
    //the constructor with the Autowired annotation get to be injected.
    //You can test thing by moving the annotation to the other constructor.
    //I did and it worked.
    public TmpService(EmployeeService1 ss1){
        this.ss1 = ss1;

    }

    public TmpService(){

    }



    @Override
    public String toString() {
        return "TmpService{" +
                "ss1=" + ss1 +
                ", ss2=" + ss2 +
                '}';
    }
}


class EmployeeService1 {
}

class EmployeeService2 {
}

