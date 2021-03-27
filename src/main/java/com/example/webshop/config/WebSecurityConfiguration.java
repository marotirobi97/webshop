//package com.example.webshop.config;
//
//import org.springframework.boot.devtools.remote.server.HttpStatusHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.firewall.RequestRejectedException;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@EnableWebSecurity
//@EnableWebMvc
//@Configuration
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("admin")
//                .roles("ADMIN");
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/admin/*").hasRole("ADMIN")
//                .and().formLogin().defaultSuccessUrl("/admin/adminPage")
//                .and().logout().logoutSuccessUrl("/index").deleteCookies("JSESSIONID");
//
//        http.authorizeRequests()
//                .antMatchers("/static/**")
//                .permitAll();
//    }
//
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//}