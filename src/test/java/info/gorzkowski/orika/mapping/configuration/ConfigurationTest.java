package info.gorzkowski.orika.mapping.configuration;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import ma.glasnost.orika.*;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMap;
import ma.glasnost.orika.metadata.FieldMap;
import ma.glasnost.orika.metadata.ScoringClassMapBuilder;
import org.junit.Test;

import java.util.*;

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


    public enum Position {
        FIRST,
        LAST;
    }

    public static class Container {
        public long longValue;
        public String stringValue;
        public List<String> listOfString;
        public String[] arrayOfString;
        public int[] arrayOfInt;
        public Map<String, Object> map;
        public Position enumValue;
    }

    @Test
    public void shouldNotMapNull() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();

        Container a = new Container();
        Container b = new Container();

        b.longValue = 1L;
        b.stringValue = "TEST A";
        b.arrayOfString = new String[]{"a", "b", "c"};
        b.arrayOfInt = new int[]{1, 2, 3};
        b.listOfString = Arrays.asList("l1", "l2");
        b.map = Collections.singletonMap("key", (Object) "value");
        b.enumValue = Position.FIRST;

        //when
        mapperFactory.getMapperFacade().map(a, b);

        //then
        assertThat(b.stringValue).isNotNull();
        assertThat(b.arrayOfString).isNotNull();
        assertThat(b.arrayOfInt).isNotNull();
        assertThat(b.listOfString).isNotNull();
        assertThat(b.map).isNotNull();
        assertThat(b.enumValue).isNotNull();
    }

    @Test
    public void shouldMapNull() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(true).build();

        Container a = new Container();
        Container b = new Container();

        b.longValue = 1L;
        b.stringValue = "TEST A";
        b.arrayOfString = new String[]{"a", "b", "c"};
        b.arrayOfInt = new int[]{1, 2, 3};
        b.listOfString = Arrays.asList("l1", "l2");
        b.map = Collections.singletonMap("key", (Object) "value");
        b.enumValue = Position.FIRST;

        //when
        mapperFactory.getMapperFacade().map(a, b);

        //then
        assertThat(b.stringValue).isNull();
        assertThat(b.arrayOfString).isNull();
        assertThat(b.arrayOfInt).isNull();
        assertThat(b.listOfString).isNull();
        assertThat(b.map).isNull();
        assertThat(b.enumValue).isNull();
    }

    @Test
    public void shouldMapNulls_ClassLevel() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();

        mapperFactory.classMap(Container.class, Container.class)
                .mapNulls(true).byDefault().register();

        Container a = new Container();
        Container b = new Container();

        b.longValue = 1L;
        b.stringValue = "TEST A";
        b.arrayOfString = new String[]{"a", "b", "c"};
        b.arrayOfInt = new int[]{1, 2, 3};
        b.listOfString = Arrays.asList("l1", "l2");
        b.map = Collections.singletonMap("key", (Object) "value");
        b.enumValue = Position.FIRST;

        //when
        mapperFactory.getMapperFacade().map(a, b);

        //then
        assertThat(b.stringValue).isNull();
        assertThat(b.arrayOfString).isNull();
        assertThat(b.arrayOfInt).isNull();
        assertThat(b.listOfString).isNull();
        assertThat(b.map).isNull();
        assertThat(b.enumValue).isNull();
    }

    @Test
    public void shouldNotMapNulls_ClassLevel() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(true).build();

        mapperFactory.classMap(Container.class, Container.class)
                .mapNulls(false).byDefault().register();

        Container a = new Container();
        Container b = new Container();

        b.longValue = 1L;
        b.stringValue = "TEST A";
        b.arrayOfString = new String[]{"a", "b", "c"};
        b.arrayOfInt = new int[]{1, 2, 3};
        b.listOfString = Arrays.asList("l1", "l2");
        b.map = Collections.singletonMap("key", (Object) "value");
        b.enumValue = Position.FIRST;

        //when
        mapperFactory.getMapperFacade().map(a, b);

        //then
        assertThat(b.stringValue).isNotNull();
        assertThat(b.arrayOfString).isNotNull();
        assertThat(b.arrayOfInt).isNotNull();
        assertThat(b.listOfString).isNotNull();
        assertThat(b.map).isNotNull();
        assertThat(b.enumValue).isNotNull();
    }

    @Test
    public void shouldMap_ClassLevel() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(true).build();

        mapperFactory.classMap(Container.class, Container.class)
                .mapNulls(false).mapNullsInReverse(false)
                .field("stringValue", "stringValue")
                .field("arrayOfString", "arrayOfString")
                .mapNulls(true).mapNullsInReverse(true)
                .byDefault().register();

        Container a = new Container();
        Container b = new Container();

        b.longValue = 1L;
        b.stringValue = "TEST A";
        b.arrayOfString = new String[]{"a", "b", "c"};
        b.arrayOfInt = new int[]{1, 2, 3};
        b.listOfString = Arrays.asList("l1", "l2");
        b.map = Collections.singletonMap("key", (Object) "value");
        b.enumValue = Position.FIRST;

        //when
        mapperFactory.getMapperFacade().map(a, b);

        //then
        assertThat(b.stringValue).isNotNull();
        assertThat(b.arrayOfString).isNotNull();
        assertThat(b.arrayOfInt).isNull();
        assertThat(b.listOfString).isNull();
        assertThat(b.map).isNull();
        assertThat(b.enumValue).isNull();
    }

    @Test
    public void shouldMapNulls_FieldLevel() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();

        mapperFactory.classMap(Container.class, Container.class)
                .mapNulls(false)
                .fieldMap("arrayOfString").mapNulls(true).add()
                .byDefault().register();

        Container a = new Container();
        Container b = new Container();

        b.longValue = 1L;
        b.stringValue = "TEST A";
        b.arrayOfString = new String[]{"a", "b", "c"};
        b.arrayOfInt = new int[]{1, 2, 3};
        b.listOfString = Arrays.asList("l1", "l2");
        b.map = Collections.singletonMap("key", (Object) "value");
        b.enumValue = Position.FIRST;

        //when
        mapperFactory.getMapperFacade().map(a, b);

        //then
        assertThat(b.stringValue).isNotNull();
        assertThat(b.arrayOfString).isNull();
        assertThat(b.arrayOfInt).isNotNull();
        assertThat(b.listOfString).isNotNull();
        assertThat(b.map).isNotNull();
        assertThat(b.enumValue).isNotNull();
    }

    @Test
    public void shouldCustomizeMapping() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("age", "currentAge")
                .byDefault()
                .customize(
                        new CustomMapper<BasicPerson, BasicPersonDto>() {
                            @Override
                            public void mapAtoB(BasicPerson basicPerson, BasicPersonDto basicPersonDto, MappingContext context) {
                                Joiner joiner = Joiner.on(" ");
                                String fullName = joiner.appendTo(new StringBuilder(basicPerson.getName()).append(" "),
                                        basicPerson.getNameParts()).toString();

                                basicPersonDto.setFullName(fullName);
                            }
                        }
                )
                .register();

        BasicPerson bp = createBasicPerson("Jan", 20, Calendar.getInstance().getTime());
        bp.setNameParts(Lists.asList("von", new String[]{"Kowalski"}));

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFullName()).isEqualTo("Jan von Kowalski");
        assertThat(result.getCurrentAge()).isEqualTo(bp.getAge());
        assertThat(result.getBirthDate()).isNotNull();
    }

    public static class Source {
        public String lastName;
        public Integer age;
        public PostalAddress postalAddress;
        public String firstName;
        public String stateOfBirth;
        public String eyeColor;
        public String driversLicenseNumber;
    }

    public static class Name {
        public String first;
        public String middle;
        public String last;
    }

    public static class Destination {
        public Name name;
        public Integer currentAge;
        public String streetAddress;
        public String birthState;
        public String countryCode;
        public String favoriteColor;
        public String id;
    }

    public static class PostalAddress {
        public String street;
        public String city;
        public String state;
        public String postalCode;
        public Country country;
    }

    public static class Country {
        public String name;
        public String alphaCode;
        public int numericCode;
    }

    @Test
    public void shouldCustomizeClassMapBuilder() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().classMapBuilderFactory(new ScoringClassMapBuilder.Factory()).build();


        //when
        ClassMap<Source, Destination> map = mapperFactory.classMap(Source.class, Destination.class).byDefault().toClassMap();

        Map<String, String> mapping = new HashMap<String, String>();
        for (FieldMap f : map.getFieldsMapping()) {
            mapping.put(f.getSource().getExpression(), f.getDestination().getExpression());
        }

        //then
        assertThat("name.first").isEqualTo(mapping.get("firstName"));
        assertThat("name.last").isEqualTo(mapping.get("lastName"));
        assertThat("streetAddress").isEqualTo(mapping.get("postalAddress.street"));
        assertThat("countryCode").isEqualTo(mapping.get("postalAddress.country.alphaCode"));
        assertThat("currentAge").isEqualTo(mapping.get("age"));
        assertThat("birthState").isEqualTo(mapping.get("stateOfBirth"));

        assertThat(mapping.containsKey("driversLicenseNumber")).isFalse();
        assertThat(mapping.containsKey("eyeColor")).isFalse();
    }

    @Test
    public void shouldMapWithCustomizedClassMapBuilder() {
        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().classMapBuilderFactory(new ScoringClassMapBuilder.Factory()).build();

        mapperFactory.classMap(Source.class, Destination.class).byDefault().register();

        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        Source src = new Source();
        src.firstName = "Jan";
        src.postalAddress = new PostalAddress();
        src.postalAddress.country = new Country();
        src.postalAddress.country.alphaCode = "PL";

        //when
        Destination result = mapperFacade.map(src, Destination.class);

        //then
        assertThat(result.name.first).isEqualTo("Jan");
        assertThat(result.countryCode).isEqualTo("PL");
    }


}