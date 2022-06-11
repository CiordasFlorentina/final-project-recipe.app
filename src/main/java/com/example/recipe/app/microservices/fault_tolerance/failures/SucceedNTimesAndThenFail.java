package com.example.recipe.app.microservices.fault_tolerance.failures;

public class SucceedNTimesAndThenFail implements PotentialFailure {

  int n;
  int successCount;

  public SucceedNTimesAndThenFail(int n) {
    this.n = n;
  }

  @Override
  public void occur() {
    if (successCount < n) {
      successCount++;
      return;
    }
    throw new RuntimeException("Error occurred while gettign ingredients...");
  }
}
