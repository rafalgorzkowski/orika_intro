package info.gorzkowski.orika.filter;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.NullFilter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.fest.assertions.Delta;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class FilterTest {

    public static class SecurityFilter extends NullFilter<Object, Object> {

        private final String MASK = "*************";

        public boolean filtersDestination() {
            return true;
        }

        @Override
        public <S, D> boolean shouldMap(Type<S> sourceType, String sourceName, S source, Type<D> destType, String destName, MappingContext mappingContext) {
            if ("age".equals(sourceName) || "address.street".equals(sourceName)) {
                return false;
            }
            return true;
        }

        @SuppressWarnings("unchecked")
        public <D> D filterDestination(D destinationValue, final Type<?> sourceType, final String sourceName, final Type<D> destType,
                                       final String destName, final MappingContext mappingContext) {
            if ("creditCardNumber".equals(sourceName)) {
                String cardMask = (String) destinationValue;
                destinationValue = (D) (MASK.substring(0, cardMask.length() - 4) + cardMask.substring(cardMask.length() - 4));
            }
            return destinationValue;

        }
    }

    @Test
    public void shouldUseSecurityFilter() {
        //given
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        factory.classMap(Source.class, Destination.class)
                .field("address.street", "street")
                .field("address.city", "city")
                .byDefault().register();
        factory.registerFilter(new SecurityFilter());

        MapperFacade mapper = factory.getMapperFacade();

        Source source = new Source();
        source.name = new SourceName();
        source.name.first = "Jan";
        source.name.last = "Kowalski";
        source.id = 1L;
        source.age = 25;
        source.cost = 0.99d;
        source.creditCardNumber = "5432109876543210";
        source.address = new SourceAddress();
        source.address.street = "Postepu";
        source.address.city = "WAW";

        //when
        Destination dest = mapper.map(source, Destination.class);

        //then
        assertThat(dest.age).isNull();
        assertThat(dest.street).isNull();
        assertThat(dest.id).isEqualTo(Long.valueOf(source.id));
        assertThat(source.name.first).isEqualTo(dest.name.first);
        assertThat(source.name.last).isEqualTo(dest.name.last);
        assertThat(source.cost).isEqualTo(dest.cost.doubleValue(), Delta.delta(0.01d));
        assertThat("************3210").isEqualTo(dest.creditCardNumber);
        assertThat(source.address.city).isEqualTo(dest.city);

    }

    public static class Source {
        public SourceName name;
        public Long id;
        public int age;
        public double cost;
        public String creditCardNumber;
        public SourceAddress address;
    }

    public static class SourceName {
        public String first;
        public String last;
    }

    public static class SourceAddress {
        public String street;
        public String city;
    }

    public static class Destination {
        public DestinationName name;
        public Long id;
        public Integer age;
        public BigDecimal cost;
        public String creditCardNumber;
        public String street;
        public String city;
    }

    public static class DestinationName {
        public String first;
        public String last;

    }

}
