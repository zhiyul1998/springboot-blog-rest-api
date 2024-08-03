package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@Configuration // this class becomes a Java based configuration within which we can define all the spring bean definitions.
@EnableMethodSecurity // allow method level authentication config
@SecurityScheme( // swagger ui: http://localhost:8080/swagger-ui/index.html
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

  private UserDetailsService userDetailsService; // inject this class to retrieve user data from database
                                                 // inject interface to achieve the loose coupling
  private JwtAuthenticationEntryPoint authenticationEntryPoint;
  private JwtAuthenticationFilter authenticationFilter;

  public SecurityConfig(UserDetailsService userDetailsService,
                        JwtAuthenticationEntryPoint authenticationEntryPoint,
                        JwtAuthenticationFilter authenticationFilter) {
    this.userDetailsService = userDetailsService;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.authenticationFilter = authenticationFilter;
  }

  @Bean // encode the password from a plain text
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // config Authentication Manager in the Spring Security Config class
  @Bean // authentication manager automatically gets the userDetailService and passwordEncoder to do the database authentication
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf((csrf) -> csrf.disable()) // disable csrf
            .authorizeHttpRequests((authorize) ->
                    //authorize.anyRequest().authenticated()) // require authentication for any request
                    authorize.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                             .requestMatchers("/api/v1/auth/**").permitAll()
                             .requestMatchers("/swagger-ui/**").permitAll()
                             .requestMatchers("/v3/api-docs/**").permitAll()
                             .anyRequest().authenticated() // permit all the authenticated users to access get and auth endpoints
            ).httpBasic(Customizer.withDefaults() // only enable the basic authentication
            ).exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint)
            ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // add jwt authentication filter before spring security filter
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build(); // returns an instance of SecurityFilterChain that represents the configured security filter chain that Spring Security will use to process HTTP requests.
  }

//  @Bean // authentication manager will use this to get username and password
//        // this is called in-memory authentication since user details are stored in the application code which is only useful when testing or small application.
//        // database authentication is when user details are stored in a database
//  public UserDetailsService userDetailsService() {
//    // create two users
//    UserDetails hazel = User.builder()
//            .username("hazel")
//            .password(passwordEncoder().encode("hazel"))
//            .roles("USER")
//            .build();
//    UserDetails admin = User.builder()
//            .username("admin").
//            password(passwordEncoder().encode("admin")).
//            roles("ADMIN").
//            build();
//
//    return new InMemoryUserDetailsManager(hazel, admin);
//  }
}
