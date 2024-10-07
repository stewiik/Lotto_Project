package pl.lotto.domain.resultchecker;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class WinnersRetriever {

    private static final int NUMBERS_WHEN_PLAYER_WON = 3;

    List<Player> retrieveWinners(List<Ticket> allTicketsByDate, Set<Integer> winningNumbers) {
        return allTicketsByDate.stream()
                .map(ticket -> {
                    Set<Integer> hitNumbers = calculateHits(winningNumbers, ticket);
                    return buildPlayer(ticket, hitNumbers);
                })
                .toList();
    }

    private Player buildPlayer(Ticket ticket, Set<Integer> hitNumbers) {
        Player.PlayerBuilder playerBuilder = Player.builder();
        if (isWinner(hitNumbers)) {
            playerBuilder.isWinner(true);
        }
        return playerBuilder
                .hash(ticket.hash())
                .numbers(ticket.numbersFromUser())
                .hitNumbers(hitNumbers)
                .drawDate(ticket.drawDate())
                .build();
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() >= NUMBERS_WHEN_PLAYER_WON;
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, Ticket ticket) {
       return ticket.numbersFromUser().stream()
               .filter(winningNumbers::contains)
               .collect(Collectors.toSet());
    }
}
