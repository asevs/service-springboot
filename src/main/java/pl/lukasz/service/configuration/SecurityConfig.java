package pl.lukasz.service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
    }

    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec
                .authorizeRequests()                                                                                      // authorization request
                .antMatchers("/").permitAll()                                                                 // access for everyone
                .antMatchers("/index").permitAll()                                                            // access for everyone
                .antMatchers("/login").permitAll()                                                            // access for everyone
                .antMatchers("/register").permitAll()                                                         // access for everyone
                .antMatchers("/adduser").permitAll()                                                          // access for everyone
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")                                             // access only for admin
                .anyRequest().authenticated()                                                                             // access only for logged-in users
                .and().csrf().disable()
                .formLogin()
                .loginPage("/login")                                                                                      //
                .failureUrl("/login?error=true")                                                                          // when login failed
                .defaultSuccessUrl("/").usernameParameter("email")                                                        // when login success
                .passwordParameter("password")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))                         //logout = clear session
                .logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/denied");
    }

    public void configure(WebSecurity webSec) throws Exception {
        webSec.ignoring()
                .antMatchers("/resources/**", "/statics/**", "/css/**", "/js/**", "/images/**", "/incl/**");   // SpringBoot dont need authorization when you want access for this data
    }

}