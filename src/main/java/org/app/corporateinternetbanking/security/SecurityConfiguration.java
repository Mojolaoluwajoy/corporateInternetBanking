package org.app.corporateinternetbanking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity )throws Exception{
        String[] publicEndPoints=new String[]{"/auth/**","/user/register","/organizations/create"};
        httpSecurity.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth ->auth
                .requestMatchers(publicEndPoints)
                        .permitAll()
                         .requestMatchers("/users/invitation/").hasRole("ADMIN")
                        .requestMatchers("/organizations/findBy","/organization/viewAll").hasRole("SUPERADMIN")
                        .requestMatchers("/organizations/approve/").hasRole("SUPER_ADMIN")
                        .requestMatchers("/accounts/create/").hasRole("ADMIN")
                        .requestMatchers("/transactions/initiate").hasRole("MAKER")
                        .requestMatchers("/transactions/approve").hasRole("APPROVER")
                       .requestMatchers("/currencies/**").hasRole("SUPER_ADMIN")
                        .anyRequest().authenticated() ).sessionManagement(session ->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new  DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());
        return  provider;
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }






}
