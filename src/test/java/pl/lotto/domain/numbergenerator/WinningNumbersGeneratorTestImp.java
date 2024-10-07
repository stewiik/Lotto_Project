package pl.lotto.domain.numbergenerator;

import java.util.Set;

public class WinningNumbersGeneratorTestImp implements RandomNumbersGenerable {

    private final Set<Integer> generatedNumbers;

    WinningNumbersGeneratorTestImp() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    WinningNumbersGeneratorTestImp(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers() {
        return SixRandomNumbersDto.builder()
                .numbers(generatedNumbers)
                .build();
    }
}
