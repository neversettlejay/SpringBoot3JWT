package com.jaytech.security.configurations.filters;

import com.jaytech.security.models.payload.transfer.EntityCreatedEvent;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "RequestLoggingFilter", urlPatterns = "/*")
@Component
@Slf4j
public class RequestLoggingFilter implements Filter {

    // Adding final keyword creates its instance as we have added required args constructor
    private final ApplicationEventPublisher applicationEventPublisher;

    public RequestLoggingFilter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public void publishHttpServletRequestCreatedEvent(HttpServletRequest request) {
        EntityCreatedEvent entityCreatedEvent = new EntityCreatedEvent(this, request);
        applicationEventPublisher.publishEvent(entityCreatedEvent);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("Processing '" + request.getRequestURI() + "' URI using filters");
        publishHttpServletRequestCreatedEvent(request);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
