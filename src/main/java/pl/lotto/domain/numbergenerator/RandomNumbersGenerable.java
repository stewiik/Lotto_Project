package pl.lotto.domain.numbergenerator;


import pl.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;

public interface RandomNumbersGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand);
}
