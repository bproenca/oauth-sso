package br.com.bcp;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

    http.cors().and()
      .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/foos/**")
            .hasAuthority("SCOPE_read")
        .antMatchers(HttpMethod.PUT, "/api/foos/**")
            .hasAuthority("SCOPE_write")
        .antMatchers(HttpMethod.GET, "/actuator/**")
            .hasRole("actuadoradmin")
        .antMatchers(HttpMethod.GET, "/api/public/**")
            .permitAll()
        .anyRequest()
            .authenticated()
        .and()
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
  }
  
}