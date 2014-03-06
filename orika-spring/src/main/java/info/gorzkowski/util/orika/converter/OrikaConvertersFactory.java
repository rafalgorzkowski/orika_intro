package info.gorzkowski.util.orika.converter;

import info.gorzkowski.domain.Product;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

import java.util.List;

/**
 * Sets up converters used to by DTO mappers.
 *
 * @author rgorzkowski
 */
public class OrikaConvertersFactory extends AbstractConverterFactory {

    /**
     * Converts id into {@link Product}.
     *
     * @author rgorzkowski
     */
    private static class LongToProductConverter extends CustomConverter<Long, Product> {
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return Long.class == sourceType.getRawType() && Product.class == destinationType.getRawType();
        }

        @Override
        public Product convert(Long source, Type<? extends Product> destinationType) {
            Product p = new Product();
            p.setId(source);
            return p;
        }
    }


    @Override
    protected List<Converter<?, ?>> createConverters(List<Converter<?, ?>> converters) {
        // TODO make generic converters to handle any identifiable DTO
        converters.add(new LongToProductConverter());
        return converters;
    }

}
