package screensaver;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import screensaver.PeriodicalScopeConfigurer;

@Component
public class CustomScopeRejisteryBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory factory) throws BeansException {
            factory.registerScope("periodical", new PeriodicalScopeConfigurer());
    }
}
