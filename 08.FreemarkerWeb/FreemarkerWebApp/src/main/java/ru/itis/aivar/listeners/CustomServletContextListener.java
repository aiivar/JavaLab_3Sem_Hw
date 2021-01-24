package ru.itis.aivar.listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.aivar.repositories.CookiesRepository;
import ru.itis.aivar.repositories.CookiesRepositoryImpl;
import ru.itis.aivar.repositories.UsersRepository;
import ru.itis.aivar.repositories.UsersRepositoryJdbcImpl;
import ru.itis.aivar.services.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class CustomServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        Properties properties = new Properties();
        try {
            properties.load(servletContext.getResourceAsStream("/WEB-INF/db.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setDriverClassName(properties.getProperty("db.driver.classname"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.max-pool-size")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        servletContext.setAttribute("dataSource", dataSource);

        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(dataSource);
        UsersService usersService = new UsersServiceImpl(usersRepository);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        SecurityService securityService = new SecurityServiceImpl(passwordEncoder, usersService);

        CookiesRepository cookiesRepository = new CookiesRepositoryImpl(dataSource);
        CookieService cookieService = new CookieServiceImpl(cookiesRepository, usersRepository);

        servletContext.setAttribute("cookieService", cookieService);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("usersService", usersService);
        servletContext.setAttribute("passwordEncoder", passwordEncoder);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
