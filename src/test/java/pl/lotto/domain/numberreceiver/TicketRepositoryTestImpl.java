package pl.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TicketRepositoryTestImpl implements TicketRepository {

    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket savedTicket) {
        tickets.put(savedTicket.hash(), savedTicket);
        return savedTicket;
    }

    @Override
    public List<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate) {
        return tickets.values()
                .stream()
                .filter(ticket -> ticket.drawDate().equals(drawDate))
                .toList();
    }

    @Override
    public Ticket findByHash(String hash) {
        return tickets.get(hash);
    }
}
