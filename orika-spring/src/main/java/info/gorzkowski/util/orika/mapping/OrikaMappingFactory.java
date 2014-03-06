package info.gorzkowski.util.orika.mapping;

import info.gorzkowski.domain.Plan;
import info.gorzkowski.domain.Product;
import info.gorzkowski.dto.PlanDto;
import info.gorzkowski.dto.ProductDto;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMap;

import java.util.List;

/**
 * The class creates class mappings for requests module that could be used by orika factory
 * {@link OrikaMapperFacadeFactory}
 *
 * @author rgorzkowski
 */
public class OrikaMappingFactory extends AbstractClassMappingFactory {

    @Override
    protected List<ClassMap<?, ?>> createMappings(List<ClassMap<?, ?>> classMappings) {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        classMappings.add(
                mapperFactory.classMap(Product.class, ProductDto.class).byDefault().toClassMap()
        );

        classMappings.add(
                mapperFactory.classMap(PlanDto.class, Plan.class)
                        .fieldMap("productId", "product")
                        .aToB()
                        .add()
                        .byDefault()
                        .toClassMap()
        );

        return classMappings;
    }

}
