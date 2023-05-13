package com.jaytech.security.configurations;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class PropertyConfigurations {

    private final Environment environment;

    // Define properties for all environments
    @PostConstruct
    public void init() {
        String activeProfile = environment.getActiveProfiles()[0];

        //Will fetch only the currently active environment
        String configFileName = "application-" + activeProfile + ".yaml";

        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new ClassPathResource("application.yaml"), new ClassPathResource(configFileName));
        Properties properties = factory.getObject();

        PropertiesPropertySource propertySource = new PropertiesPropertySource(configFileName, properties);
        MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
        propertySources.addFirst(propertySource);
    }

    public String getSpringProfilesActive() {
        return environment.getProperty("spring.profiles.active");
    }

    public String getSecretKey() {
        return environment.getProperty("jay-tech.application.security.secret-key");
    }

    public String getAdminAccessKey() {
        return environment.getProperty("jay-tech.application.security.admin-data-access-key");
    }

}
