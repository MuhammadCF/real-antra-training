package antratraining.week2.aop;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyMethodInvocation implements MethodInvocation{

    private List<MethodInterceptor> methodInterceptorList;
    private Object realInstance;
    private Method method;
    private int idx;

    public ProxyMethodInvocation(List<MethodInterceptor> methodInterceptorList, Object realInstance, Method method) {
        this.methodInterceptorList = methodInterceptorList;
        this.realInstance = realInstance;
        this.method = method;
    }

    @Override
    public Object proceed() throws Exception {
        if(idx < methodInterceptorList.size()) {
            MethodInterceptor mi = methodInterceptorList.get(idx);
            idx++;
            return mi.invoke(this);
        } else {
            return method.invoke(realInstance);
        }
    }
}
