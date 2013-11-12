package info.gorzkowski.orika.basic;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: rafal
 * Date: 12.11.13
 * Time: 23:11
 */
public class SimpleMappingTest {

    @Test
    public void shouldMapParson() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Person.class, PersonDestination.class)
                .field("firstName", "givenName")
                .field("lastName", "sirName")
                .field("age", "currentAge")
                .byDefault()
                .register();

        Person src = Person.PersonBuilder.person()
                .withFirstName("Jan")
                .withLastName("Kowalski")
                .withAge(18)
                .withSex(Sex.M)
                .build();

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        PersonDestination personDestination = mapperFacade.map(src, PersonDestination.class);

        //then
        assertThat(personDestination.getGivenName()).isEqualTo("Jan");
        assertThat(personDestination.getSirName()).isEqualTo("Kowalski");
        assertThat(personDestination.getCurrentAge()).isEqualTo(18);
    }

    @Test
    public void shouldMapUsingBoundMapper() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Person.class, PersonDestination.class)
                .field("firstName", "givenName")
                .field("lastName", "sirName")
                .field("age", "currentAge")
                .byDefault()
                .register();

        Person src = Person.PersonBuilder.person()
                .withFirstName("Jan")
                .withLastName("Kowalski")
                .withAge(18)
                .withSex(Sex.M)
                .build();

        //when
        BoundMapperFacade<Person, PersonDestination> boundMapper =
                mapperFactory.getMapperFacade(Person.class, PersonDestination.class);

        PersonDestination personDestination = boundMapper.map(src);

        //then
        assertThat(personDestination.getGivenName()).isEqualTo("Jan");
        assertThat(personDestination.getSirName()).isEqualTo("Kowalski");
        assertThat(personDestination.getCurrentAge()).isEqualTo(18);
    }

    @Test
    public void shouldUseMapReverse() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Person.class, PersonDestination.class)
                .field("firstName", "givenName")
                .field("lastName", "sirName")
                .field("age", "currentAge")
                .byDefault()
                .register();

        PersonDestination personDestination = PersonDestination.PersonDestinationBuilder.personDestination()
                .withGivenName("Jan")
                .withSirName("Kowalski")
                .withCurrentAge(18)
                .build();

        //when
        BoundMapperFacade<Person, PersonDestination> boundMapper =
                mapperFactory.getMapperFacade(Person.class, PersonDestination.class);

        Person person = boundMapper.mapReverse(personDestination);

        //then
        assertThat(person.getFirstName()).isEqualTo("Jan");
        assertThat(person.getLastName()).isEqualTo("Kowalski");
        assertThat(person.getAge()).isEqualTo(18);
    }
}

