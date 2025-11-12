package snpsu.mca.javaFS.Guardian.Eye.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Allow public access to ALL pages including dashboard
                .requestMatchers(
                    "/", "/login", "/signup", "/family-login", "/family-signup",
                    "/dashboard", "/dashboard/**",  // Allow dashboard without auth
                    "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/api/users", "/api/users/**",
                    "/api/family", "/api/family/**"
                ).permitAll()
                
                // Only protect specific admin pages (if any)
                // .requestMatchers("/admin/**").authenticated()
                
                .anyRequest().permitAll() // Allow everything else
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}