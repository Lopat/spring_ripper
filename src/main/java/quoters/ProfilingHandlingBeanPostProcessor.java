package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Controller;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProfilingHandlingBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> map = new HashMap <>();
    private ProfilingController controller = new ProfilingController();


    ProfilingHandlingBeanPostProcessor() throws Exception{
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectInstance objectInstance = server.registerMBean(controller, new ObjectName("profiling", "name",
                                                                                        "controller"));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class <?> beanClass = bean.getClass();
        if(beanClass.isAnnotationPresent(Profiling.class))
            map.put(beanName, beanClass);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = map.get(beanName);

        if(beanClass != null){
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (controller.isEnabled()) {
                        long before = System.nanoTime();
                        System.out.println("PROFILING");
                        Object returnValue = method.invoke(bean, args);
                        System.out.println("DONE");
                        long after = System.nanoTime();

                        System.out.println(after-before);
                        return returnValue;
                    }else {
                        return method.invoke(bean, args);
                    }
                }
            });
        }

        return bean;
    }
}
