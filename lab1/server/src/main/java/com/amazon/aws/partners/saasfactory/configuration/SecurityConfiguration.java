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
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/webfonts/**").permitAll()
                .antMatchers("/index.html**").permitAll()
                .antMatchers("/health.html").permitAll()
                .antMatchers("/products/**").authenticated()
                .antMatchers("/orders/**").authenticated()
                .antMatchers("/dashboard.html").authenticated()
                .and()
            .formLogin()
                .loginPage("/index.html?signin=1")
                .loginProcessingUrl("/")
                .failureUrl("/index.html?error=true")
                .defaultSuccessUrl("/dashboard.html")
                .and()
            .logout()
                .logoutSuccessUrl("/index.html")
                .deleteCookies("JSESSIONID")
                .and()
            .csrf().disable();
        ;
    }
}
