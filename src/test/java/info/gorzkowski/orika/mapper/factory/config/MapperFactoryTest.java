package info.gorzkowski.orika.mapper.factory.config;

import info.gorzkowski.orika.mapping.configuration.BasicPerson;
import info.gorzkowski.orika.mapping.configuration.BasicPersonDto;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import org.junit.Test;

import java.util.Calendar;

import static org.fest.assertions.Assertions.assertThat;

public class MapperFactoryTest {

    @Test
    public void shouldGenerateSrcCode() {
        //given
//        System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");
//        System.setProperty(OrikaSystemProperties.COMPILER_STRATEGY,EclipseJdtCompilerStrategy.class.getName());

        //given
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .compilerStrategy(new EclipseJdtCompilerStrategy())
                .build();

        mapperFactory.classMap(BasicPerson.class, BasicPersonDto.class)
                .field("name", "fullName")
                .field("age", "currentAge")
                .register();

        BasicPerson bp = new BasicPerson();
        bp.setName("Jan");
        bp.setAge(20);
        bp.setBirthDate(Calendar.getInstance().getTime());

        //when
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        BasicPersonDto result = mapperFacade.map(bp, BasicPersonDto.class);

        //then
        assertThat(result.getFullName()).isEqualTo(bp.getName());
        assertThat(result.getCurrentAge()).isEqualTo(bp.getAge());
        assertThat(result.getBirthDate()).isNull();

    }
}
