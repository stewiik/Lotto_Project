package pl.lotto.domain.numberreceiver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record Ticket(String hash, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}