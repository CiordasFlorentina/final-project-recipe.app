package com.example.recipe.app.microservices.fault_tolerance.failures;

public class NoFailure implements PotentialFailure {
    @Override
    public void occur() {
    }
}
