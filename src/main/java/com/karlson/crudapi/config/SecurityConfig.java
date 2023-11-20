package com.karlson.crudapi.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
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
    private static final String SWAGER_UI_PATTERN = "/swagger-ui/**";
    private static final String API_DOCS = "/api-docs/**";
/*
    @Bean
    @Deprecated
    MvcRequestMatcher.Builder h2mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector)
                .servletPath("/h2-console/*");
    }

 */

    /* dev profile uses a H2 database.       When the console is enabled the issues with "multiple servlets" This is a workaround for that */
    @Bean
    @Profile("dev")
    // https://stackoverflow.com/questions/77024398/spring-h2db-web-console-this-method-cannot-decide-whether-these-patterns-are
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrfConfigurer ->
                        csrfConfigurer
                                .ignoringRequestMatchers(
                                        mvcMatcherBuilder.pattern(API_URL_PATTERN), // todo make this correct, implementing correct metods.
                                        mvcMatcherBuilder.pattern(HOME_PATTERN),
                                        mvcMatcherBuilder.pattern(SWAGER_UI_PATTERN),
                                        mvcMatcherBuilder.pattern(API_DOCS),
                                        PathRequest.toH2Console()))

                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .authorizeHttpRequests(auth ->
                                auth
                                        .requestMatchers(mvcMatcherBuilder.pattern(API_URL_PATTERN)).permitAll()
                                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, SWAGER_UI_PATTERN)).permitAll()
                                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, API_DOCS)).permitAll()
                                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, HOME_PATTERN)).permitAll()
                                        //This line is optional in .authenticated() case as .anyRequest().authenticated()
                                        //would be applied for H2 path anyway
                                        .requestMatchers(PathRequest.toH2Console()).permitAll()
//                                .anyRequest().authenticated() // this locks things down
                                        .anyRequest().permitAll() // todo remove
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    @Profile("!dev")
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
}
