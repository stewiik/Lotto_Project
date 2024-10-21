package pl.lotto.domain.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {

    private static final int NUM_OF_NUMBERS_FROM_USER = 6;
    private static final int MIN_NUMBER_FROM_USER = 1;
    private static final int MAX_NUMBER_FROM_USER = 99;

    List<ValidationResult> errors;

    List<ValidationResult> validate(Set<Integer> numbersFromUsers) {
        errors = new LinkedList<>();
        if (!isNumbersSizeEqualsSix(numbersFromUsers)) {
            errors.add(ValidationResult.NOT_SIX_NUMBERS_GIVEN);
        }
        if (!isNumberInRange(numbersFromUsers)) {
            errors.add(ValidationResult.NOT_IN_RANGE);
        }
        return errors;
    }

    private boolean isNumbersSizeEqualsSix(Set<Integer> numbersFromUsers) {
        return numbersFromUsers.size() == NUM_OF_NUMBERS_FROM_USER;
    }

    boolean isNumberInRange(Set<Integer> numbersFromUsers) {
        return numbersFromUsers.stream()
                .allMatch(number -> number >= MIN_NUMBER_FROM_USER && number <= MAX_NUMBER_FROM_USER);
    }

    String createResultMessage() {
        return this.errors
                .stream()
                .map(validationResult -> validationResult.message)
                .collect(Collectors.joining(","));
    }
}
