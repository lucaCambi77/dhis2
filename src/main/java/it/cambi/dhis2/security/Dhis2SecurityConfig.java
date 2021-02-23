package it.cambi.dhis2.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class Dhis2SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${dhis2.api.username}")
  private String dhis2Username;

  @Value("${dhis2.api.password}")
  private String dhis2Password;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication()
        .withUser(dhis2Username)
        .password(passwordEncoder().encode(dhis2Password))
        .roles("ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/dhis2/v1/api/**")
        .hasRole("ADMIN");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
