package pl.lotto.domain.numbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class NumbersGeneratorConfig {

    @Bean
    WinningNumbersRepository repository() {
        return new WinningNumbersRepository() {
            @Override
            public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
                return Optional.empty();
            }

            @Override
            public boolean existsByDate(LocalDateTime nextDrawDate) {
                return false;
            }

            @Override
            public WinningNumbers save(WinningNumbers winningNumbers) {
                return null;
            }
        };
    }

    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade, RandomNumbersGenerable randomNumberGenerator, WinningNumbersGeneratorFacadeConfigProperties properties) {
        WinningNumbersValidator winningNumberValidator = new WinningNumbersValidator();
        return new WinningNumbersGeneratorFacade(randomNumberGenerator, winningNumberValidator, winningNumbersRepository, numberReceiverFacade, properties);
    }

    WinningNumbersGeneratorFacade createForTests(RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersGeneratorFacadeConfigProperties properties = WinningNumbersGeneratorFacadeConfigProperties.builder()
                .upperBand(99)
                .lowerBand(1)
                .count(6)
                .build();
        return winningNumbersGeneratorFacade(winningNumbersRepository, numberReceiverFacade, generator, properties);
    }
}