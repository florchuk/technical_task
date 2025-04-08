package technikal.task.fishmarket.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import technikal.task.fishmarket.enums.Authorities;
import technikal.task.fishmarket.models.User;
import technikal.task.fishmarket.services.UserRepository;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (String username) -> {
            Optional<User> optionalUserEntity = userRepository.findByUsername(username);

            if (optionalUserEntity.isEmpty()) {
                throw new UsernameNotFoundException("...");
            }

            return optionalUserEntity.get();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(Customizer.withDefaults())
                .formLogin(
                        formLoginCustomizer -> formLoginCustomizer
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticate")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                )
                .logout(
                        logoutCustomizer -> logoutCustomizer
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(HttpMethod.GET, "/fish/create", "/fish/delete")
                                .hasAuthority(Authorities.ADMIN.getAuthority())
                                .requestMatchers(HttpMethod.POST, "/fish/create")
                                .hasAuthority(Authorities.ADMIN.getAuthority())
                                .requestMatchers(HttpMethod.GET, "/login", "/login/")
                                .anonymous()
                                .requestMatchers(HttpMethod.POST, "/logout")
                                .authenticated()
                                .anyRequest()
                                .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
