package antratraining.week2.aop;

public interface MethodInterceptor {
    Object invoke(MethodInvocation methodInvocation) throws Exception;
}
