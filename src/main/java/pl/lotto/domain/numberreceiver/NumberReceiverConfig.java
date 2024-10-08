package pl.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

@Configuration
public class NumberReceiverConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    HashGenerable hashGenerable() {
        return new HashGenerator();
    }

    @Bean
    TicketRepository ticketRepository() {
        return new TicketRepository() {
            @Override
            public Collection<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate) {
                return null;
            }

            @Override
            public Ticket findByHash(String hash) {
                return null;
            }

            @Override
            public Ticket save(Ticket savedTicket) {
                return null;
            }
        };
    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, ticketRepository, drawDateGenerator, hashGenerator);
    }

}
