package pl.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TicketRepository {

    Ticket save(Ticket savedTicket);

    Collection<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate);

    Ticket findByHash(String hash);

}
