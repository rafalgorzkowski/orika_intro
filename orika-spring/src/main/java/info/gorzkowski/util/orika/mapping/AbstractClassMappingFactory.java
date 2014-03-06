package info.gorzkowski.util.orika.mapping;

import java.util.LinkedList;
import java.util.List;
import ma.glasnost.orika.metadata.ClassMap;
import org.springframework.beans.factory.config.AbstractFactoryBean;

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

    protected abstract List<ClassMap<?, ?>> createMappings(List<ClassMap<?, ?>> classMappings);

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

}