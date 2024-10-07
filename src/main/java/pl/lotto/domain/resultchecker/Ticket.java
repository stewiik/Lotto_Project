package pl.lotto.domain.resultchecker;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record Ticket(String hash, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
