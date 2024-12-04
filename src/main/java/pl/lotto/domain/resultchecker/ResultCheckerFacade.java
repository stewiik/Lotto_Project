package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.PlayerDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultannouncer.ResultMessageResponse.*;

@AllArgsConstructor
public class ResultCheckerFacade {

    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    private final WinnersRetriever winnersRetriever;
    private final PlayerRepository playerRepository;

    public PlayerDto generateResults() {

        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDtoToTicket(allTicketsByDate);

        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();

        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayerDto.builder()
                    .message("Winning numbers failed to generate. Try again later.")
                    .build();
        }

        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);

        return PlayerDto.builder()
                .results(ResultCheckerMapper.mapPlayersToResultDto(players))
                .message("Winners successfully retrieved")
                .build();
    }

    public ResultDto findByTicketHash(String hash) {
        Player playerByHash = playerRepository.findByHash(hash).orElse(null);
        if (playerByHash == null) {
            List<String> ticketIds = numberReceiverFacade.retrieveAllTicketsByNextDrawDate()
                    .stream().map(TicketDto::hash).toList();
            boolean isTicketWaitingForDraw = ticketIds.contains(hash);
            if (isTicketWaitingForDraw) {
                if (!winningNumbersGeneratorFacade.areWinningNumbersGeneratedByDate()) {
                    return ResultDto.builder()
                            .hash(hash)
                            .numbers(Set.of())
                            .drawDate(numberReceiverFacade.retrieveNextDrawDate())
                            .isWinner(false)
                            .message(String.valueOf(WAIT_MESSAGE))
                            .build();
                }
                return ResultDto.builder()
                        .hash(hash)
                        .numbers(Set.of())
                        .drawDate(numberReceiverFacade.retrieveNextDrawDate())
                        .isWinner(false)
                        .message(String.valueOf(LOSE_MESSAGE))
                        .build();
            }
            throw new PlayerResultNotFoundException("Not found for id: " + hash);
        }
        return ResultDto.builder()
                .hash(playerByHash.hash())
                .numbers(playerByHash.numbers())
                .hitNumbers(playerByHash.hitNumbers())
                .drawDate(playerByHash.drawDate())
                .isWinner(playerByHash.isWinner())
                .build();
    }
}
