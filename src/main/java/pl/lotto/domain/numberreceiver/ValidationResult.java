package pl.lotto.domain.numberreceiver;

enum ValidationResult {
    NOT_SIX_NUMBERS_GIVEN("You should give 6 unique numbers"),
    NOT_IN_RANGE("You should give numbers in range 1-99"),
    INPUT_SUCCESS("Success");

    final String message;

    ValidationResult(String message) {
        this.message = message;
    }
}
