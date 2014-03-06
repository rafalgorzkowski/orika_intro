package info.gorzkowski.orika.converter;


import info.gorzkowski.domain.Product;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

public class LongToProductConverter extends CustomConverter<Long, Product> {
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
