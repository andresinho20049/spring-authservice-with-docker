package com.andresinho20049.authservice.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andresinho20049.authservice.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author André Carlos <https://github.com/andresinho20049>
 */
@Slf4j
@Configuration
public class OAuth2ServerConfiguration {

	private static final String RESOURCE_ID = "restservice";

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId(RESOURCE_ID);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.cors().and().csrf().disable()
				.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers("/v2/api-docs", 
						"/configuration/ui", 
						"/swagger-resources/**", 
						"/configuration/security", 
						"/swagger-ui.html", 
						"/swagger-ui/**", 
						"/webjars/**")
					.permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilterAfter(new LogRequestFilter(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Value("${security.client-id}")
		private String clientId;

		@Value("${security.client-secret}")
		private String clientSecret;

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		private UserDetailsServiceImpl userDetailsService;

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Autowired
		private RedisConnectionFactory redisConnectionFactory;

		@Bean
		TokenStore tokenStore() {
			return new RedisTokenStore(redisConnectionFactory);
		}

		@Bean
		JwtAccessTokenConverter accessTokenConverter() {
			return new JwtAccessTokenConverter();
		}

		@Bean
		@Primary
		DefaultTokenServices tokenServices() {
			DefaultTokenServices tokenServices = new DefaultTokenServices();
			tokenServices.setSupportRefreshToken(true);
			tokenServices.setTokenStore(tokenStore());
			return tokenServices;
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
			tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), accessTokenConverter()));

			endpoints
				.tokenStore(tokenStore())
				.authenticationManager(this.authenticationManager)
				.userDetailsService(userDetailsService)
				.tokenEnhancer(tokenEnhancerChain)
				.accessTokenConverter(accessTokenConverter())
				.exceptionTranslator(e -> {
						if (e instanceof InvalidGrantException) {
							OAuth2Exception auth2Exception = (OAuth2Exception) e;
							return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(auth2Exception);
						} else if (e instanceof OAuth2Exception) {
							OAuth2Exception auth2Exception = (OAuth2Exception) e;
							return ResponseEntity.status(auth2Exception.getHttpErrorCode()).body(auth2Exception);
						} else {
							throw e;
						}
					});
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) {
			security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.passwordEncoder(this.passwordEncoder);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients
				.inMemory()
				.withClient(clientId)
				.authorizedGrantTypes("password", "authorization_code", "refresh_token")
				.scopes("all")
				.refreshTokenValiditySeconds(100000)
				.resourceIds(RESOURCE_ID)
				.secret(passwordEncoder.encode(clientSecret))
				.accessTokenValiditySeconds(50000);

		}

		@EventListener
		public void authFailedEventListener(AbstractAuthenticationFailureEvent e) {
			// Note that the param is Abstract authentication,
			// so all authentication and authorization failures will be heard

			log.error("Authentication failure, user: " + e.getAuthentication().getName() + " - Exception: "
					+ e.getException());
		}

	}

}
