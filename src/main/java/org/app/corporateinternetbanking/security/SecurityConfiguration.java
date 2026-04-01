package org.app.corporateinternetbanking.security;

import lombok.RequiredArgsConstructor;
import org.app.corporateinternetbanking.user.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
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
                         .requestMatchers("/users/invitation/").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/organizations/findBy","/organization/viewAll").hasRole(UserRole.SUPER_ADMIN.name())
                        .requestMatchers("/organizations/approve/").hasRole(UserRole.SUPER_ADMIN.name())
                        .requestMatchers("/accounts/create/").hasRole(UserRole.ADMIN.name())
                      .requestMatchers("/transactions/initiate").hasRole(UserRole.MAKER.name())
                        .requestMatchers("/transactions/approve").hasRole(UserRole.APPROVER.name())
                      .requestMatchers("/transactions/pending").hasAnyRole(UserRole.APPROVER.name(),UserRole.ADMIN.name())
                       .requestMatchers("/currencies/status/").hasRole(UserRole.SUPER_ADMIN.name())
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
