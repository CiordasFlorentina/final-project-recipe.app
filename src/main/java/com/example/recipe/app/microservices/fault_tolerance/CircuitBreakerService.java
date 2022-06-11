package com.example.recipe.app.microservices.fault_tolerance;

import com.example.recipe.app.microservices.fault_tolerance.failures.AlwaysSlowNSeconds;
import com.example.recipe.app.microservices.fault_tolerance.failures.SucceedNTimesAndThenFail;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.service.IngredientService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Service
public class CircuitBreakerService {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public List<Ingredient> TimeoutFailure(){
        ResponseEntity<Ingredient> response =
                restTemplate.getForEntity("http://localhost:8081/ingredient", Ingredient.class);
        return Collections.emptyList();
    }

    private final String CIRCUIT_BREAKER_INGREDIENTS_PROPERTY = "IngredientService"; //used in application.yml

    @CircuitBreaker(name=CIRCUIT_BREAKER_INGREDIENTS_PROPERTY, fallbackMethod = "ingredientFallback")
    public List<Ingredient> GetIngredientsTimeoutError() throws Exception{
       return TimeoutFailure();
    }

    public List<Ingredient> ingredientFallback(Exception e){
        System.out.println("CircuitBreaker fallback method... Couldn't get the ingredients :(");
        return Collections.emptyList();
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    void displayDefaultValues() {
        CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
        System.out.println("failureRateThreshold = " + config.getFailureRateThreshold());
        System.out.println("minimumNumberOfCalls = " + config.getMinimumNumberOfCalls());
        System.out.println("permittedNumberOfCallsInHalfOpenState = " + config.getPermittedNumberOfCallsInHalfOpenState());
        System.out.println("maxWaitDurationInHalfOpenState = " + config.getMaxWaitDurationInHalfOpenState());
        System.out.println("slidingWindowSize = " + config.getSlidingWindowSize());
        System.out.println("slidingWindowType = " + config.getSlidingWindowType());
        System.out.println("slowCallRateThreshold = " + config.getSlowCallRateThreshold());
        System.out.println("slowCallDurationThreshold = " + config.getSlowCallDurationThreshold());
        System.out.println("automaticTransitionFromOpenToHalfOpenEnabled = " + config.isAutomaticTransitionFromOpenToHalfOpenEnabled());
        System.out.println("writableStackTraceEnabled = " + config.isWritableStackTraceEnabled());
    }

   public List<Ingredient> countBasedSlidingWindow_FailedCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(70.0f)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = registry.circuitBreaker("IngredientService");

        ingredientService.setPotentialFailure(new SucceedNTimesAndThenFail(10));

        Supplier<List<Ingredient>> ingredientsSupplier = () -> ingredientService.getIngredients();
        Supplier<List<Ingredient>> decoratedIngredientsSupplier = circuitBreaker.decorateSupplier(ingredientsSupplier);

        for (int i=0; i<20; i++) {
            try {
                System.out.println("Getting results...");
                System.out.println(decoratedIngredientsSupplier.get());
            }
            catch (Exception e) {
                System.out.println("Circuit breaker should be open and not permit further requests..");
//                e.printStackTrace();
            }
        }
        return decoratedIngredientsSupplier.get();
    }


    public List<Ingredient> countBasedSlidingWindow_SlowCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5) // max number of requests before OPEN CIRCUIT
                .slowCallRateThreshold(70.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = registry.circuitBreaker("IngredientService");

        ingredientService.setPotentialDelay(new AlwaysSlowNSeconds(2));

        Supplier<List<Ingredient>> ingredientSupplier = circuitBreaker.decorateSupplier(() -> ingredientService.getIngredients());

        for (int i=0; i<20; i++) {
            try {
                System.out.println("Getting results...");
                System.out.println(ingredientSupplier.get());
            }
            catch (Exception e) {
                System.out.println("Circuit breaker should be open and not permit further requests..");
//                e.printStackTrace();
            }
        }
        return ingredientSupplier.get();
    }

    public List<Ingredient> countBasedSlidingWindow_Failed_And_SlowCalls() {
        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(70.0f)
                .slowCallRateThreshold(70.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = registry.circuitBreaker("IngredientService");

        ingredientService.setPotentialDelay(new AlwaysSlowNSeconds(2));

        Supplier<List<Ingredient>> ingredientSupplier = circuitBreaker.decorateSupplier(() -> ingredientService.getIngredients());

        for (int i=0; i<20; i++) {
            try {
                System.out.println("Getting results...");
                System.out.println(ingredientSupplier.get());
            }
            catch (Exception e) {
                System.out.println("Circuit breaker should be half/open and not permit further requests..");
//                e.printStackTrace();
            }
        }
        return ingredientSupplier.get();
    }


}
