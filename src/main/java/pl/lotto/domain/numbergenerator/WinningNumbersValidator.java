package pl.lotto.domain.numbergenerator;

import java.util.Set;

public class WinningNumbersValidator {

    private final int LOWER_BAND = 1;
    private final int UPPER_BAND = 99;

    public Set<Integer> validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            throw new IllegalStateException("Number out of range!");
        }
        return winningNumbers;
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < LOWER_BAND || number > UPPER_BAND);
    }
}
