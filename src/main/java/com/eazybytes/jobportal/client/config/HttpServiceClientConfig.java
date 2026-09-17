package com.eazybytes.jobportal.client.config;

import com.eazybytes.jobportal.client.service.PostServices;
import com.eazybytes.jobportal.client.service.TodoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "posts", types = {PostServices.class})
@ImportHttpServices(group = "todos", types = {TodoService.class})
public class HttpServiceClientConfig {

    @Bean
    public RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> {
            groups.filterByName("todos").forEachClient(
                    (group, restClientBuilder) -> {
                        restClientBuilder.baseUrl("https://jsonplaceholder.typicode.com/todos")
                                .requestInterceptor((request, body, execution) -> {
                                    return execution.execute(request, body);
                                }).build();
                    }
            );

            groups.filterByName("posts").forEachClient(
                    (group, restClientBuilder) -> {
                        restClientBuilder.baseUrl("https://jsonplaceholder.typicode.com/posts")
                                .requestInterceptor((request, body, exection) -> {
                                    return exection.execute(request, body);
                                }).build();
                    }
            );
        };
    }
}
