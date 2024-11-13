package pl.lotto.domain.numberreceiver;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
record Ticket(String hash, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
