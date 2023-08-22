package com.andresinho20049.authservice.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

@Configuration
public class SwaggerConfig {

	private static final String ACCESS_TOKEN = "JWT";
	private static final String RESOURCE_ID = "restservice";

	@Value("${security.client-id}")
	private String CLIENT_ID;

	@Value("${security.client-secret}")
	private String CLIENT_SECRET;

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.andresinho20049.authservice"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(this.apiInfo())
		        .securitySchemes(Arrays.asList(securityScheme()))
		        .securityContexts(Arrays.asList(securityContext()));

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("AuthService with Spring Security")
				.description("API Rest with Oauth2 autoconfigure")
				.contact(
						new Contact("André Carlos", "https://github.com/andresinho20049", "andre.andresinho2009@hotmail.com"))
				.license("MIT").version("1.0.0").build();
	}

	@Bean
	public SecurityConfiguration security() {
	    return SecurityConfigurationBuilder.builder()
	    	.appName("Spring Oauth2 - AuthService")
	        .clientId(CLIENT_ID)
	        .clientSecret(CLIENT_SECRET)
	        .scopeSeparator("")
	        .realm(RESOURCE_ID)
	        .useBasicAuthenticationWithAccessCodeGrant(true)
	        .build();
	}
	
	private SecurityScheme securityScheme() {
		
		GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/api/oauth/token");

	    SecurityScheme oauth = new OAuthBuilder()
	    	.name(ACCESS_TOKEN)
	        .grantTypes(Arrays.asList(grantType))
	        .scopes(Arrays.asList(scopes()))
	        .build();
	    
	    return oauth;
	}
	
	private AuthorizationScope[] scopes() {
	    AuthorizationScope[] scopes = { 
	      new AuthorizationScope("all", "accessEverything") };
	    return scopes;
	}
	
	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	      .securityReferences(defaultAuth())
	      .build();
	}
	
	private List<SecurityReference> defaultAuth() {
        return Arrays.asList(
            new SecurityReference(ACCESS_TOKEN, scopes()));
    }

	

}