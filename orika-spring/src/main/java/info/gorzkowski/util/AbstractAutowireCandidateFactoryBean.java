package info.gorzkowski.util;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * Used to implements {@link FactoryBean}, which result will be used as autowiring candidate.
 * 
 * @author rgorzkowski
 */
public abstract class AbstractAutowireCandidateFactoryBean<T> extends AbstractFactoryBean<T> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected final T createInstance() {
        final T instance = doCreateInstance();
        if (instance != null) {
            applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);
        }
        return instance;
    }

    protected abstract T doCreateInstance();
}
