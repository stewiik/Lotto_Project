package pl.lotto.domain.numbergenerator;


public interface RandomNumbersGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand);
}
