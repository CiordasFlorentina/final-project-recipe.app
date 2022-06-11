package com.example.recipe.app.microservices.fault_tolerance.failures;

public class NoDelay implements PotentialDelay {
    @Override
    public void occur() {
    }
}
