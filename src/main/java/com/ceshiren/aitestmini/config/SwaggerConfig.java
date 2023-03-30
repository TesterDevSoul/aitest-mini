package com.ceshiren.aitestmini.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author www.testeru.top
 * @version 1.0.0
 * @Project top.testeru.mini.config
 * @Description swaggerapi
 * 对于 spring-boot v3 支持，请确保使用 springdoc-openapi v2
 * @createTime 2023年03月27日 18:45:13
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("aitest-mini系统")// 页面标题
                .description("aitest-mini接口文档")// 描述
                .contact(new Contact("gaigai", "https://ceshiren.com/", ""))// 创建人信息
                .version("1.0.0")//API项目版本号
                .build();
    }


    @Bean
    public Docket docket() {
        //Swagger 的配置主要围绕Docket bean 进行：
        return new Docket(DocumentationType.OAS_30)
                //配置是否启用Swagger，如果是false，在浏览器将无法访问，默认是true
                .enable(true)
                .groupName("aitestmini_interface")
                .apiInfo(apiInfo())
//                .globalRequestParameters(globalRequestParameters())
//在定义Docket bean 之后，它的select()方法返回一个ApiSelectorBuilder的实例，它提供了一种控制 Swagger 暴露的端点的方法
                .select()
                //any 任何请求都扫描  none   任何请求都不扫描
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.ceshiren.aitestmini"))
                .paths(PathSelectors.any()).build();
//我们可以在RequestHandlerSelectors和PathSelectors的帮助下配置用于选择RequestHandler的谓词。
// 对两者都使用any()将使我们的整个 API 的文档可以通过 Swagger 获得。
    }

    //生成全局通用参数
    private List<RequestParameter> globalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        //   公共请求参数生成器-token
        RequestParameter tokenParameter = new RequestParameterBuilder()
                .in(ParameterType.HEADER)//在swagger里显示header
                .name("token")//header的参数名为 token
                .description("对应的token值")

                .required(true)//对应参数是否为必传，如果不是必传参数则设置为false
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)))

                .build();
        //为消费者提供帮助建立模型  更新标量类型
        //   公共请求参数生成器-udid
        RequestParameter udidParameter = new RequestParameterBuilder()
                .in(ParameterType.QUERY)//在swagger里显示header
                .name("udid")//header的参数名为 token
                .description("设备的ID")
                .required(false)//对应参数是否为必传，如果不是必传参数则设置为false
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)))
                .build();

        parameters.add(tokenParameter);
        parameters.add(udidParameter);
        return parameters;
//        Collections.singletonList(parameterBuilder.build());
    }
}