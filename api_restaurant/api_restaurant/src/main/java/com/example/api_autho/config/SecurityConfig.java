package com.example.api_autho.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String MAKE_ORDER_ENDPOINT = "/api/order/make";
    private static final String SHOW_ORDER_ENDPOINT = "/api/order/show";
    private static final String SHOW_MENU_ENDPOINT = "/api/menu/show";
    private static final String ADD_DISH_ENDPOINT = "/api/dish/add";
    private static final String DELETE_DISH_ENDPOINT = "/api/dish/delete";
    private static final String CHANGE_DISH_ENDPOINT = "/api/dish/change";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(MAKE_ORDER_ENDPOINT, SHOW_ORDER_ENDPOINT, SHOW_MENU_ENDPOINT,
                             DELETE_DISH_ENDPOINT, ADD_DISH_ENDPOINT, CHANGE_DISH_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and();
    }
    // feignclient
    // resttemplate
}
