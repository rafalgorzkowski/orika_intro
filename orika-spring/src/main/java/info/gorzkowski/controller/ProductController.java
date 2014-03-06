package info.gorzkowski.controller;

import info.gorzkowski.domain.Plan;
import info.gorzkowski.domain.Product;
import info.gorzkowski.dto.PlanDto;
import info.gorzkowski.dto.ProductDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;

@Controller
public class ProductController {

    @Resource(name = "product2DtoConverter")
    private Converter<Product, ProductDto> product2DtoConverter;

    @Resource(name = "dto2PlanConverter")
    private Converter<PlanDto, Plan> dto2PlanConverter;

    @Resource(name = "productList2DtoConverter")
    private Converter<List<Product>, List<ProductDto>> productList2DtoConverter;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ModelAndView getData() {
        ModelAndView model = new ModelAndView("index");

        List<Product> list = getProductList();

        List<ProductDto> productDtos = productList2DtoConverter.convert(list);

        model.addObject("lists", productDtos);

        return model;
    }

    private List<Product> getProductList() {
        List<Product> list = new ArrayList<Product>();
        list.add(createProduct(1L, "Product1"));
        list.add(createProduct(2L, "Product2"));
        return list;
    }

    private Product createProduct(long id, String name) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        return product;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ModelAndView getProduct(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("product");

        Product product = createProduct(id, "Product " + id);

        ProductDto convert = product2DtoConverter.convert(product);
        model.addObject("product", convert);

        return model;
    }

    @RequestMapping(value = "/createPlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Plan createPlan(@RequestBody final PlanDto planDto) {
        Plan plan = dto2PlanConverter.convert(planDto);
        return plan;
    }

}
