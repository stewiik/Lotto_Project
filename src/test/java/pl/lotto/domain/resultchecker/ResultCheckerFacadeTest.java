package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.PlayerDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultCheckerFacadeTest {

    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = mock(WinningNumbersGeneratorFacade.class);
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final PlayerRepository playerRepository = new PlayerRepositoryTestImpl();

    @Test
    public void should_generate_all_players_with_correct_message() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(TicketDto.builder()
                                .hash("001")
                                .numbersFromUser(Set.of(1,2,3,4,5,6))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbersFromUser(Set.of(1,2,7,8,9,10))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbersFromUser(Set.of(7,8,9,10,11,12))
                                .drawDate(drawDate)
                                .build())
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfig().resultCheckerFacade(winningNumbersGeneratorFacade, numberReceiverFacade, playerRepository);
        //when
        PlayerDto playerDto = resultCheckerFacade.generateResults();
        //then
        List<ResultDto> results = playerDto.results();
        ResultDto resultDto1 = ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto resultDto2 = ResultDto.builder()
                .hash("002")
                .numbers(Set.of(1,2,7,8,9,10))
                .hitNumbers(Set.of(1, 2))
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        ResultDto resultDto3 = ResultDto.builder()
                .hash("003")
                .numbers(Set.of(7,8,9,10,11,12))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        assertThat(results).contains(resultDto1, resultDto2, resultDto3);
        String message = playerDto.message();
        String expectedMessage = "Winners succeeded to retrieve";
        assertThat(message).isEqualTo(expectedMessage);
    }

    @Test
    public void should_generate_fail_message_when_winningNumbers_equal_null() {
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(null)
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfig().resultCheckerFacade(winningNumbersGeneratorFacade, numberReceiverFacade, playerRepository);
        //when
        PlayerDto playerDto = resultCheckerFacade.generateResults();
        //then
        String expectedMessage = "Winners failed to retrieve";
        String message = playerDto.message();
        assertThat(message).isEqualTo(expectedMessage);
    }

    @Test
    public void should_generate_fail_message_when_winningNumbers_is_empty() {
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of())
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfig().resultCheckerFacade(winningNumbersGeneratorFacade, numberReceiverFacade, playerRepository);
        //when
        PlayerDto playerDto = resultCheckerFacade.generateResults();
        //then
        String expectedMessage = "Winners failed to retrieve";
        String message = playerDto.message();
        assertThat(message).isEqualTo(expectedMessage);
    }

    @Test
    public void should_generate_result_with_correct_credentials() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        String hash = "001";
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(TicketDto.builder()
                                .hash(hash)
                                .numbersFromUser(Set.of(7,8,9,10,11,13))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbersFromUser(Set.of(7,8,9,10,11,14))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbersFromUser(Set.of(7,8,9,10,11,12))
                                .drawDate(drawDate)
                                .build())
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfig().resultCheckerFacade(winningNumbersGeneratorFacade, numberReceiverFacade, playerRepository);
        resultCheckerFacade.generateResults();
        //when
        ResultDto resultDto = resultCheckerFacade.findByTicketId(hash);
        //then
        ResultDto expectedResult = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(7,8,9,10,11,13))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        assertThat(resultDto).isEqualTo(expectedResult);
    }
}
