package sk.lukaspatrnciak.blogapplication.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
                        auth.requestMatchers(antMatcher("/css/**")).permitAll();
                        auth.requestMatchers(antMatcher("/js/**")).permitAll();
                        auth.requestMatchers(antMatcher("/images/**")).permitAll();;
                        auth.requestMatchers(antMatcher("/")).permitAll();
                        auth.requestMatchers(antMatcher("/register/**")).permitAll();
                        auth.requestMatchers(antMatcher("/posts/**")).permitAll();
                        auth.requestMatchers(PathRequest.toH2Console()).permitAll();
                        auth.anyRequest().authenticated();
                })

                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error")
                    .permitAll()
                )

                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );


        return http.build();
    }
}
