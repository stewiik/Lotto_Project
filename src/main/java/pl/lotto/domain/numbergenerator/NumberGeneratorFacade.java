package pl.lotto.domain.numbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class NumberGeneratorFacade {

    private final NumberReceiverFacade numberReceiverFacade;
    private final NumbersGenerable winningNumbersGenerator;
    private WinningNumbersValidator winningNumbersValidator;
    private WinningNumbersRepository winningNumbersRepository;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = winningNumbersGenerator.generateSixRandomNumbers();
        winningNumbersValidator.validate(winningNumbers);
        WinningNumbers numbersToSaveInRepository = WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .date(nextDrawDate)
                .build();
        winningNumbersRepository.save(numbersToSaveInRepository);

        return WinningNumbersMapper.mapFromWinningNumbersToWinningNumbersDto(numbersToSaveInRepository);
    }

    public WinningNumbersDto receiveWinningNumbersByDate(LocalDateTime date) {
        WinningNumbers numbersByDate = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not found"));
        return WinningNumbersMapper.mapFromWinningNumbersToWinningNumbersDto(numbersByDate);
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }
}
