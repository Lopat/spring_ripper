package quoters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;

public class PostProxyInvokerContextListener implements ApplicationListener <ContextRefreshedEvent> {

    @Autowired
    private ConfigurableListableBeanFactory factory;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        String[] names = applicationContext.getBeanDefinitionNames();

        for(String name : names){
            BeanDefinition definition = factory.getBeanDefinition(name);
            String originalClassName = definition.getBeanClassName();

            try {
                Class <?> originalClass = Class.forName(originalClassName);
                for(Method m : originalClass.getMethods()){
                    if(m.isAnnotationPresent(PostProxy.class)){
                        Object bean = applicationContext.getBean(name);
                        Class <?> proxyClass = bean.getClass();
                        Method currentMethod = proxyClass.getMethod(m.getName(), m.getParameterTypes());
                        currentMethod.invoke(bean);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
