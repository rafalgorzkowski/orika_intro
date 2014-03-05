package info.gorzkowski.orika.converter;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

public class ConverterTest {
    @Test
    public void shouldMapBigDecimalToPrimtiveDouble() {
        //given
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.getConverterFactory().registerConverter(new CustomConverter<BigDecimal, Double>() {
            public Double convert(BigDecimal source, Type<? extends Double> destinationType) {
                return new Double(source.doubleValue());
            }
        });

        BigDecimalClass source = new BigDecimalClass();
        source.setValue(BigDecimal.TEN);

        //when
        DoubleClass dest = factory.getMapperFacade().map(source, DoubleClass.class);

        //then
        assertThat((Double) dest.getValue()).isEqualTo(new Double(10));

    }

    public static class BigDecimalClass {
        private BigDecimal value;

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

    }

    public static class DoubleClass {
        private double value;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

    }

    // converter can also be registered at a field level
    @Test
    @Ignore
    public void shouldMapDateToCalendar() {
        //given
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(Product.class, ProductDTO.class)
                .field("tempDate", "tempCal")
                .register();

        factory.getConverterFactory().registerConverter(new BidirectionalConverter<Date, Calendar>() {

            @Override
            public Calendar convertTo(Date source, Type<Calendar> destinationType) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(source);
                cal.add(Calendar.HOUR, 1);
                return cal;
            }

            @Override
            public Date convertFrom(Calendar source, Type<Date> destinationType) {
                return source.getTime();
            }

        });
        MapperFacade mapper = factory.getMapperFacade();

        Product p = new Product();
        p.setTempDate(Calendar.getInstance().getTime());

        //when
        ProductDTO result = mapper.map(p, ProductDTO.class);

        //then
        assertThat(result.getTempCal().getTime()).isEqualTo(p.getTempDate());
    }

    public static class Product {

        private Date tempDate;

        public Date getTempDate() {
            return tempDate;
        }

        public void setTempDate(Date tempDate) {
            this.tempDate = tempDate;
        }

    }

    public static class ProductDTO {

        private Calendar tempCal;

        public Calendar getTempCal() {
            return tempCal;
        }

        public void setTempCal(Calendar tempCal) {
            this.tempCal = tempCal;
        }

    }

    @Test
    public void shouldMapDateTime() {
        //given
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(Time.class, TimeDTO.class)
                .byDefault()
                .register();

        factory.getConverterFactory().registerConverter(new PassThroughConverter(DateTime.class));

        Time source = new Time();
        source.setValue(DateTime.now());

        //when
        TimeDTO dest = factory.getMapperFacade().map(source, TimeDTO.class);

        //then
        assertThat(dest.value).isSameAs(source.value);

    }

    public static class Time {
        private DateTime value;

        public DateTime getValue() {
            return value;
        }

        public void setValue(DateTime value) {
            this.value = value;
        }
    }

    public static class TimeDTO {
        private DateTime value;

        public DateTime getValue() {
            return value;
        }

        public void setValue(DateTime value) {
            this.value = value;
        }
    }
}
