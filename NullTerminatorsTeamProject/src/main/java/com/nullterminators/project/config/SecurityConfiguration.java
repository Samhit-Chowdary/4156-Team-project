package com.nullterminators.project.config;

import com.nullterminators.project.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Basic auth security configuration for the API'S.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Autowired private CompanyService companyService;

  /**
   * This method configures the basic security filter chain. CSRF is disabled.
   * The endpoint for registering a company is permitted for all users. All other
   * endpoints must be authenticated. The default HTTP Basic authentication is used.
   *
   * @param httpSecurity the http security object
   * @return the security filter chain
   * @throws Exception if an error occurs
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers("/registerCompany")
                    .permitAll()
                    .requestMatchers("/")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .httpBasic(Customizer.withDefaults());
    return httpSecurity.build();
  }


  /**
   * Returns an {@link AuthenticationProvider} that uses the company service to
   * authenticate users. The {@link PasswordEncoder} is set to a
   * {@link BCryptPasswordEncoder}.
   *
   * @return an authentication provider
   */
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(companyService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
