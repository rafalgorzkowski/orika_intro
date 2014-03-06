package info.gorzkowski.util.orika.converter;

import ma.glasnost.orika.MapperFacade;
import org.springframework.core.convert.converter.Converter;

/**
 * Orika implementation of the {@link Converter} interface. Of course you can use MapperFacade directly to map a bean,
 * but I prefer to use Converter interface with different implementations; for example: Orika, by hand and so on
 * 
 * @author rgorzkowski
 * 
 * @param <S>
 *            source type
 * @param <T>
 *            destination type
 */
public class OrikaConverter<S, T> implements Converter<S, T> {

    private Class<T> destClazz;

    private MapperFacade mapperFacade;

    public OrikaConverter(MapperFacade mapperFacade, Class<T> destClazz) {
        this.mapperFacade = mapperFacade;
        this.destClazz = destClazz;
    }

    @Override
    public T convert(S source) {
        return mapperFacade.map(source, destClazz);
    }

}