package ru.itis.aivar.csrf.spring.mvc.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import ru.itis.aivar.csrf.spring.mvc.interceptors.CsrfInterceptor;
import ru.itis.aivar.csrf.spring.mvc.repositories.UsersRepository;
import ru.itis.aivar.csrf.spring.mvc.repositories.UsersRepositoryJdbcImpl;
import ru.itis.aivar.csrf.spring.mvc.services.SecurityService;
import ru.itis.aivar.csrf.spring.mvc.services.SecurityServiceImpl;
import ru.itis.aivar.csrf.spring.mvc.services.UsersService;
import ru.itis.aivar.csrf.spring.mvc.services.UsersServiceImpl;
import ru.itis.aivar.csrf.spring.mvc.utils.Validator;


import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource({
        "classpath:db.properties"
})
@ComponentScan(basePackages = "ru.itis.aivar.csrf.spring.mvc")
public class ApplicationConfig implements WebMvcConfigurer {
    @Autowired
    Environment environment;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(csrfInterceptor());
//    }
//
//    @Bean
//    CsrfInterceptor csrfInterceptor(){
//        return new CsrfInterceptor();
//    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(environment.getProperty("db.jdbc-url"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("db.max-pool-size")));
        hikariConfig.setUsername(environment.getProperty("db.username"));
        hikariConfig.setPassword(environment.getProperty("db.password"));
        hikariConfig.setDriverClassName(environment.getProperty("db.driver-class-name"));
        return hikariConfig;
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setPrefix("");
        resolver.setSuffix(".ftlh");
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("/WEB-INF/views/");
        return configurer;
    }

    @Bean
    public UsersService usersService() {
        return new UsersServiceImpl(usersRepository());
    }

    @Bean
    public UsersRepository usersRepository() {
        return new UsersRepositoryJdbcImpl(dataSource());
    }

    @Bean
    public SecurityService securityService() {
        return new SecurityServiceImpl(new BCryptPasswordEncoder(), usersService());
    }

    @Bean
    Validator validator(){
        return new Validator();
    }

}
