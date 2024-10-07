package pl.lotto.domain.numbergenerator;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class SecureRandomNumbersGenerator implements RandomNumbersGenerable {

    private final int LOWER_BAND = 1;
    private final int UPPER_BAND = 99;
    private final int RANDOM_NUMBER_BOUND = (UPPER_BAND - LOWER_BAND) + 1;

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        while (isAmountOfNumbersLowerThanSix(winningNumbers)) {
            Random random = new SecureRandom();
            int number = random.nextInt(RANDOM_NUMBER_BOUND);
            winningNumbers.add(number);
        }
        return SixRandomNumbersDto.builder()
                .numbers(winningNumbers)
                .build();
    }

    private boolean isAmountOfNumbersLowerThanSix(Set<Integer> winningNumbers) {
        return winningNumbers.size() < 6;
    }
}
