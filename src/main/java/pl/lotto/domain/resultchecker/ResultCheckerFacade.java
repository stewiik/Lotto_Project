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

@AllArgsConstructor
public class ResultCheckerFacade {

    private NumberReceiverFacade numberReceiverFacade;
    private WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    private WinnersRetriever winnersRetriever;
    private PlayerRepository playerRepository;

    public PlayerDto generateWinners() {
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDtoToTicket(allTicketsByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();
        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayerDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }
        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);

        return PlayerDto.builder()
                .results(ResultCheckerMapper.mapPlayersToResultDto(players))
                .message("Winners succeeded to retrieve")
                .build();
    }

    public ResultDto findByHash(String hash) {
        Player player = playerRepository.findById(hash).orElseThrow(() -> new RuntimeException("Not found"));
        return ResultDto.builder()
                .hash(hash)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }
}
