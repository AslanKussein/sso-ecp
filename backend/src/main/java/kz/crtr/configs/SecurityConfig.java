package kz.crtr.configs;

import kz.crtr.security.AuthEntryPointJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtAuthenticationFilter;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setExposedHeaders(Collections.singletonList("*"));
        configuration.setAllowedHeaders(List.of("*"));
        return configuration;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.cors().configurationSource(request -> corsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/open-api/**").permitAll()
                .antMatchers(getPermitAllUrls()).permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
    }

    private String[] getPermitAllUrls() {
        return new String[]{
                "/api/v2/api-docs",
                "/api/configuration/ui",
                "/api/swagger-resources/**",
                "/api/configuration/security",
                "/api/swagger-ui.html",
                "/api/webjars/**",
                "/login"
        };
    }
}
