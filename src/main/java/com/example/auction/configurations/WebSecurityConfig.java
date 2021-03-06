package com.example.auction.configurations;
import com.example.auction.security.MainAuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MainAuthFilter mainAuthFilter;

    public WebSecurityConfig(MainAuthFilter mainAuthFilter) {
        this.mainAuthFilter = mainAuthFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .disable()
                .authorizeHttpRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/registration/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(
                        mainAuthFilter.setRequireAuthMatcher(
                                new AndRequestMatcher(new AntPathRequestMatcher("/api/private/**"))
                        ),
                        UsernamePasswordAuthenticationFilter.class
                );
    }
}
