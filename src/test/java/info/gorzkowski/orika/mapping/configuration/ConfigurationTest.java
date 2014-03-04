package info.gorzkowski.orika.mapping.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class ConfigurationTest {

    public static final String NIP = "8202373081";

    @Test
    public void shouldRegisterClassMap() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("name", "fullName")
                .field("age", "currentAge")
                .register();

        BasicPerson bp = createBasicPerson("Jan", 20, Calendar.getInstance().getTime());

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFullName()).isEqualTo(bp.getName());
        assertThat(result.getCurrentAge()).isEqualTo(bp.getAge());
        assertThat(result.getBirthDate()).isNull();

    }

    private BasicPerson createBasicPerson(String name, int age, Date time) {
        BasicPerson basicPerson = new BasicPerson();
        basicPerson.setName(name);
        basicPerson.setAge(age);
        basicPerson.setBirthDate(time);
        return basicPerson;
    }

    @Test
    public void shouldUseDefaultMapping() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("name", "fullName")
                .field("age", "currentAge")
                .byDefault()
                .register();

        BasicPerson bp = createBasicPerson("Jan", 20, Calendar.getInstance().getTime());

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFullName()).isEqualTo(bp.getName());
        assertThat(result.getCurrentAge()).isEqualTo(bp.getAge());
        assertThat(result.getBirthDate()).isNotNull();
    }

    @Test
    public void shouldUseOnlyOneDirectionMapping() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .fieldAToB("name", "fullName")
                .field("age", "currentAge")
                .field("birthDate", "birthDate")
                .register();

        BasicPerson bpSrc = createBasicPerson("Jan", 20, Calendar.getInstance().getTime());

        BasicPersonDto bpDtoSrc = new BasicPersonDto();
        bpDtoSrc.setFullName("Jan");
        bpDtoSrc.setCurrentAge(20);
        bpDtoSrc.setBirthDate(Calendar.getInstance().getTime());

        //when
        BoundMapperFacade<BasicPerson, BasicPersonDto> boundMapper =
                mapperFactory.getMapperFacade(BasicPerson.class, BasicPersonDto.class);

        BasicPersonDto bpDtoDest = boundMapper.map(bpSrc);
        BasicPerson bpDest = boundMapper.mapReverse(bpDtoSrc);

        //then
        assertThat(bpDtoDest.getCurrentAge()).isEqualTo(bpSrc.getAge());
        assertThat(bpDtoDest.getFullName()).isEqualTo(bpSrc.getName());
        assertThat(bpDtoDest.getBirthDate()).isEqualTo(bpSrc.getBirthDate());

        assertThat(bpDest.getAge()).isEqualTo(bpDtoSrc.getCurrentAge());
        assertThat(bpDest.getName()).isNull();
        assertThat(bpDest.getBirthDate()).isEqualTo(bpDtoSrc.getBirthDate());
    }

    @Test
    public void shouldExcludeFieldFromMapping() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("name", "fullName")
                .exclude("age")
                .byDefault()
                .register();

        BasicPerson bp = createBasicPerson("Jan", 20, Calendar.getInstance().getTime());

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFullName()).isEqualTo(bp.getName());
        assertThat(result.getCurrentAge()).isEqualTo(0);
        assertThat(result.getBirthDate()).isNotNull();
    }

    @Test
    public void shouldExplicitlySpecifyConstructor() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDtoWithConstructor.class)
                .constructorB("birthDate")
                .fieldAToB("name", "fullName")
//                .field("age", "currentAge")
                .byDefault()
                .register();

        BasicPerson bp = createBasicPerson("Jan", 20, Calendar.getInstance().getTime());

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDtoWithConstructor result = mapperFacade.map(bp, BasicPersonDtoWithConstructor.class);

        //then
        assertThat(result.getFullName()).isEqualTo(bp.getName());
        assertThat(result.getCurrentAge()).isEqualTo(0); //!!
        assertThat(result.getBirthDate()).isNotNull();
    }

    @Test
    public void shouldMapListElements() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("nameParts[0]", "firstName")
                .field("nameParts[1]", "lastName")
                .register();

        BasicPerson bp = new BasicPerson();
        bp.setNameParts(Lists.asList("Jan", new String[]{"Kowalski"}));

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFirstName()).isEqualTo("Jan");
        assertThat(result.getLastName()).isEqualTo("Kowalski");
        assertThat(result.getBirthDate()).isNull();
        assertThat(result.getCurrentAge()).isEqualTo(0);
        assertThat(result.getFullName()).isNull();
    }

    @Test
    public void shouldMapMapElements() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("namePartsMap[\"first\"]", "firstName")
                .field("namePartsMap['second']", "lastName")
                .register();

        BasicPerson bp = new BasicPerson();
        Map<String, String> nameParamsMap = new HashMap<String, String>();
        nameParamsMap.put("first", "Jan");
        nameParamsMap.put("second", "Kowalski");
        bp.setNamePartsMap(nameParamsMap);

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFirstName()).isEqualTo("Jan");
        assertThat(result.getLastName()).isEqualTo("Kowalski");
        assertThat(result.getBirthDate()).isNull();
        assertThat(result.getCurrentAge()).isEqualTo(0);
        assertThat(result.getFullName()).isNull();
    }

    @Test
    public void shouldMapNestedElement() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("nestedElement.nip", "nip")
                .register();

        BasicPerson bp = new BasicPerson();
        NestedElement nestedElement = new NestedElement();
        nestedElement.setNip(NIP);
        bp.setNestedElement(nestedElement);

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getNip()).isNotNull();
        assertThat(result.getNip()).isEqualTo(NIP);
        assertThat(result.getFirstName()).isNull();
        assertThat(result.getLastName()).isNull();
        assertThat(result.getBirthDate()).isNull();
        assertThat(result.getCurrentAge()).isEqualTo(0);
        assertThat(result.getFullName()).isNull();
    }
}
