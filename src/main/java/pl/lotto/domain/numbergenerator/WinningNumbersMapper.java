package pl.lotto.domain.numbergenerator;

import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;

class WinningNumbersMapper {

    static WinningNumbersDto mapFromWinningNumbersToWinningNumbersDto(WinningNumbers winningNumbers) {
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers.winningNumbers())
                .date(winningNumbers.date())
                .build();
    }

    static WinningNumbers mapFromWinningNumbersDtoToWinningNumbers(WinningNumbersDto winningNumbersDto) {
        return WinningNumbers.builder()
                .winningNumbers(winningNumbersDto.winningNumbers())
                .date(winningNumbersDto.date())
                .build();
    }
}
