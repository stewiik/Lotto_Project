package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.stream.Collectors;

class ResultCheckerMapper {

    static List<Ticket> mapFromTicketDtoToTicket(List<TicketDto> ticketsDto) {
        return ticketsDto.stream()
                .map(ticketDto -> Ticket.builder()
                        .hash(ticketDto.hash())
                        .drawDate(ticketDto.drawDate())
                        .numbersFromUser(ticketDto.numbersFromUser())
                        .build())
                .toList();
    }

    static List<ResultDto> mapPlayersToResultDto(List<Player> players) {
        return players.stream()
                .map(player -> ResultDto.builder()
                        .hash(player.hash())
                        .hitNumbers(player.hitNumbers())
                        .numbers(player.numbers())
                        .drawDate(player.drawDate())
                        .isWinner(player.isWinner())
                        .build())
                .collect(Collectors.toList());
    }
}
