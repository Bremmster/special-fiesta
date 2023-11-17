package com.karlson.crudapi.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private static final String API_URL_PATTERN = "api/v1/**";
    private static final String HOME_PATTERN = "/";

    @Bean
    @Deprecated
    MvcRequestMatcher.Builder h2mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector)
                .servletPath("/h2-console/*");
    }

    @Bean
    @Deprecated
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector)
                .servletPath("/");
    }

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/books/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/books/").permitAll();
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/books/").authenticated();
                    auth.anyRequest().authenticated();
                })
//                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
*/


    @Bean
    // https://stackoverflow.com/questions/77024398/spring-h2db-web-console-this-method-cannot-decide-whether-these-patterns-are
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrfConfigurer ->
                        csrfConfigurer
                                .ignoringRequestMatchers(
                                        mvcMatcherBuilder.pattern(API_URL_PATTERN),
                                        mvcMatcherBuilder.pattern(HOME_PATTERN),
                                        PathRequest.toH2Console()))

                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(mvcMatcherBuilder.pattern(API_URL_PATTERN)).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern(HOME_PATTERN)).permitAll()
                                //This line is optional in .authenticated() case as .anyRequest().authenticated()
                                //would be applied for H2 path anyway
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


}
