package com.amazon.aws.partners.saasfactory.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("monolith_user@example.com")
                .password(encoder.encode("Monolith123"))
                .roles("USER");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/webfonts/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/health.html").permitAll()
                .antMatchers("/products").authenticated()
                .antMatchers("/orders").authenticated()
                .antMatchers("/dashboard").authenticated()
                .and()
            .formLogin()
                .loginPage("/index?signin=1")
                .loginProcessingUrl("/")
                .failureUrl("/index?error=true")
                .defaultSuccessUrl("/dashboard")
                .and()
            .logout()
                .logoutSuccessUrl("/index")
                .deleteCookies("JSESSIONID")
                .and()
            .csrf().disable();
        ;
    }
}
