package info.gorzkowski.util.orika.mapping;

import ma.glasnost.orika.metadata.ClassMap;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class to get easier creation of new Class mapping factories, it collaborates
 * with {@link OrikaMapperFacadeFactory}.
 * 
 * @author rgorzkowski
 */
public abstract class AbstractClassMappingFactory extends AbstractFactoryBean<List<ClassMap<?, ?>>> {

    @Override
    protected List<ClassMap<?, ?>> createInstance() {
        List<ClassMap<?, ?>> classMappings = new LinkedList<ClassMap<?, ?>>();
        return createMappings(classMappings);
    }

    /**
     * Create your mappings here.
     */
    protected abstract List<ClassMap<?, ?>> createMappings(List<ClassMap<?, ?>> classMappings);

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

}