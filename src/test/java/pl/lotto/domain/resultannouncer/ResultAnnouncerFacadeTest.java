package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.ResultMessageResponse.*;

class ResultAnnouncerFacadeTest {

    ResultResponseRepository resultResponseRepository = new ResultResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);
    private Clock clock = Clock.systemUTC();

    @Test
    public void should_return_response_with_lose_message_if_ticket_is_not_winning_ticket() {
        //given
        LocalDateTime drawTime = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfig().resultAnnouncerFacade(resultResponseRepository, resultCheckerFacade, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawTime)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findByTicketHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultResponseDto resultResponseDto = ResultResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawTime)
                .isWinner(false)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(resultResponseDto, LOSE_MESSAGE.message);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_wait_message_if_date_is_before_announcement_time() {
        //given
        LocalDateTime drawTime = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        String hash = "123";
        clock = Clock.fixed(LocalDateTime.of(2023, 8, 22, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfig().resultAnnouncerFacade(resultResponseRepository, resultCheckerFacade, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 8, 9))
                .drawDate(drawTime)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByTicketHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultResponseDto resultResponseDto = ResultResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 8, 9))
                .drawDate(drawTime)
                .isWinner(true)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(resultResponseDto, WAIT_MESSAGE.message);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_hash_does_not_exist_message_if_hash_does_not_exist() {
        //given
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfig().resultAnnouncerFacade(resultResponseRepository, resultCheckerFacade, clock);
        when(resultCheckerFacade.findByTicketHash(hash)).thenReturn(null);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.message);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_already_checked_message_if_response_is_already_retrieved() {
        //given
        LocalDateTime drawTime = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        String hash = "123";
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> hitNumbers = Set.of(1, 2, 3, 4, 8, 9);
        boolean isWinner = true;
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfig().resultAnnouncerFacade(resultResponseRepository, resultCheckerFacade, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(numbers)
                .hitNumbers(hitNumbers)
                .drawDate(drawTime)
                .isWinner(isWinner)
                .build();
        when(resultCheckerFacade.findByTicketHash(hash)).thenReturn(resultDto);
        ResultAnnouncerResponseDto firstCheckResponse = resultAnnouncerFacade.checkResult(hash);
        ResultResponse resultResponse = new ResultResponse(hash, numbers, hitNumbers, drawTime, isWinner);
        resultResponseRepository.save(resultResponse);
        //when
        ResultAnnouncerResponseDto secondCheckResponse = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(firstCheckResponse.resultDto(), ResultMessageResponse.ALREADY_CHECKED.message);
        assertThat(secondCheckResponse).isEqualTo(expectedResult);
    }


    @Test
    public void should_return_response_with_win_message_if_ticket_is_winning_ticket() {
        //given
        LocalDateTime drawTime = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfig().resultAnnouncerFacade(resultResponseRepository, resultCheckerFacade, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawTime)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByTicketHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultResponseDto resultResponseDto = ResultResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawTime)
                .isWinner(true)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(resultResponseDto, WIN_MESSAGE.message);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }
}
