package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;

class ResultMapper {
    public static ResultResponseDto mapFromResultDtoToResult(ResultResponse result) {
        return ResultResponseDto.builder()
                .hash(result.hash())
                .numbers(result.numbers())
                .hitNumbers(result.hitNumbers())
                .drawDate(result.drawDate())
                .isWinner(result.isWinner())
                .build();
    }
}
