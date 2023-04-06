package tech.maiquer.common.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import tech.maiquer.common.config.property.SwaggerProperty;
import tech.maiquer.common.model.ResultCode;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
public class SwaggerConfig {

    @Resource
    private SwaggerProperty documentAutoProperties;

    @Bean
    public Docket docket() {

        List<Response> responseList = new ArrayList<>();
        Arrays.stream(ResultCode.values()).forEach(resultCode -> {
            responseList.add(
                    new ResponseBuilder().code(resultCode.getCode().toString()).description(resultCode.getMessage()).build()
            );
        });

        return new Docket(DocumentationType.OAS_30)
                .enable(documentAutoProperties.getEnable())
                .protocols(newHashSet("https", "http")) //限定通讯协议集合
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .groupName(documentAutoProperties.getGroupName())
                .globalResponses(GET, responseList)
                .globalResponses(POST, responseList)
                .globalResponses(PUT, responseList)
                .globalResponses(DELETE, responseList)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(documentAutoProperties.getScanPackage()))
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact(documentAutoProperties.getContactName(), documentAutoProperties.getContactUrl(), documentAutoProperties.getContactEmail()))
                .title(documentAutoProperties.getTitle())
                .description(documentAutoProperties.getDescription())
                .version(documentAutoProperties.getVersion())
                .build();
    }

    /**
     * 自定义一个Apikey
     * 这是一个包含在header中键名为Authorization的JWT标识
     */
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    /**
     * 配置JWT的SecurityContext 并设置全局生效
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }


}
