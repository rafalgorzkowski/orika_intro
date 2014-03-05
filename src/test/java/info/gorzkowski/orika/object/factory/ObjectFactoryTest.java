package info.gorzkowski.orika.object.factory;

import info.gorzkowski.orika.basic.Person;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeBuilder;
import ma.glasnost.orika.metadata.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import static org.fest.assertions.Assertions.assertThat;

public class ObjectFactoryTest {

    public static class JaxbTypeFactory implements ObjectFactory<JAXBElement<Person>> {

        public JAXBElement<Person> create(Object source, MappingContext mappingContext) {
            if (source instanceof Person) {
                return new JAXBElement<Person>(new QName("http://javart.eu/JAXBTest", "Person"), Person.class, (Person) source);
            }
            throw new IllegalArgumentException("source must be a Person");
        }
    }

    public static class PersonHolder {
        public Person person;
    }

    public static class PersonDTOHolder {
        public JAXBElement<Person> person;
    }

    @Test
    public void shouldUseObjectFactory() {
        //given
        MapperFactory factory = new DefaultMapperFactory.Builder()
                .build();

        factory.registerObjectFactory(new JaxbTypeFactory(), new TypeBuilder<JAXBElement<Person>>(){}.build());

        MapperFacade mapperFacade = factory.getMapperFacade();

        Person person = new Person();
        person.setFirstName("Jan");
        PersonHolder holder = new PersonHolder();
        holder.person = person;

        //when
        PersonDTOHolder dest = mapperFacade.map(holder, PersonDTOHolder.class);

        //then
        assertThat(dest).isNotNull();
        assertThat(dest.person).isNotNull();
        assertThat(dest.person.getName().getLocalPart()).isEqualTo("Person");
        assertThat(dest.person.getValue().getFirstName()).isEqualTo(person.getFirstName());

    }
}
