package com.jaytech.security.configurations;

import com.jaytech.security.configurations.filters.RequestLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@RequiredArgsConstructor
public class WebConfigurations {

    private final RequestLoggingFilter requestLoggingFilter;


    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilterRegistration() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestLoggingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("RequestLoggingFilter"); // Set the filter name
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Set the order of the filter
        return registrationBean;
    }
}
