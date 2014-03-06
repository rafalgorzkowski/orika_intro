package info.gorzkowski.util.orika.mapping;

import info.gorzkowski.util.AbstractAutowireCandidateFactoryBean;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMap;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.*;

/**
 * Creates and configures Orika {@link MapperFactory}
 * <p/>
 * example mapper facade declaration
 * <bean id="mapperFacade" class="OrikaMapperFacadeFactory">
 * <property name="classMappings">
 * <list>
 * <bean class="RequestsClassMappingFactory" />
 * </list>
 * </property>
 * <property name="converters">
 * <list>
 * <bean class="StringTocCountryConverter" />
 * </list>
 * </property>
 * </bean>
 * <p/>
 * usage: Autowire MapperFacade mapperFacade;
 * <p/>
 * mapperFacade.map(source, dest);
 * dest = defaultMapper.map(source, Dest.class);
 *
 * @author rgorzkowski
 * @see MapperFacade
 */
public class OrikaMapperFacadeFactory extends AbstractAutowireCandidateFactoryBean<MapperFacade> {

    private List<ClassMap<?, ?>> classMappings = Collections.emptyList();
    private List<Converter<?, ?>> converters = Collections.emptyList();

    @Override
    protected MapperFacade doCreateInstance() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        registerMappings(factory);
        registerConverters(factory.getConverterFactory());
        factory.getConverterFactory().registerConverter(new PassThroughConverter(DateTime.class, DateMidnight.class,
                LocalDateTime.class, LocalDate.class));

        return factory.getMapperFacade();
    }

    private void registerConverters(ConverterFactory converterFactory) {
        for (Converter<?, ?> converter : converters) {
            converterFactory.registerConverter(converter);
        }
    }

    protected void registerMappings(MapperFactory factory) {
        for (ClassMap<?, ?> mapping : classMappings) {
            factory.registerClassMap(mapping);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return MapperFacade.class;
    }

    /**
     * @param classMaps the {@link #classMappings} to set
     */
    public void setClassMappings(List<List<ClassMap<?, ?>>> classMaps) {
        this.classMappings = new LinkedList<ClassMap<?, ?>>();
        for (List<ClassMap<?, ?>> mappings : classMaps) {
            this.classMappings.addAll(mappings);
        }
    }

    /**
     * @param converters the {@link #converters} to set
     */
    public void setConverters(List<Converter<?, ?>> converters) {
        this.converters = new LinkedList<Converter<?, ?>>();
        this.converters.addAll(converters);
    }
}
