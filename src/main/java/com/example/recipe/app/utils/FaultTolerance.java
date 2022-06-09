package com.example.recipe.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.stream.IntStream;

@Component
public class FaultTolerance {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public void fakeEndpointApiCall(){
        // Fake api call->throws exception->CircuitBreaker calls fallback function.
        // For validating the CircuitBreaker functionality
        restTemplate.getForObject("http://localhost:8081/ingredient", String.class);
    }

    public void concurrentRequestsApiCall(){
        /* For validating the Bulkhead functionality, limiting the requests */
        int i=1;
        IntStream.range(i,3).parallel().forEach(t->{
            System.out.println("sending request...\n");
            String response = new RestTemplate().getForObject("http://localhost:8080/ingredient/", String.class);
            System.out.println(response);
        });
    }
}
