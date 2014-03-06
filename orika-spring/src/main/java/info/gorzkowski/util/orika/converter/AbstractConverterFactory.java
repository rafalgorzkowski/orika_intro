package info.gorzkowski.util.orika.converter;

import ma.glasnost.orika.Converter;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class to get easier creation of new converter factories, it collaborates
 * with {@link info.gorzkowski.util.orika.mapping.OrikaMapperFacadeFactory}.
 *
 * @author rgorzkowski
 */
public abstract class AbstractConverterFactory extends AbstractFactoryBean<List<Converter<?, ?>>> {

    @Override
    protected List<Converter<?, ?>> createInstance() {
        List<Converter<?, ?>> converters = new LinkedList<Converter<?, ?>>();
        return createConverters(converters);
    }

    /**
     * Create your converters here.
     */
    protected abstract List<Converter<?, ?>> createConverters(List<Converter<?, ?>> converters);

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

}
