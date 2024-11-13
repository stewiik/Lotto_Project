package pl.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

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
    NumberReceiverFacade numberReceiverFacade(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, ticketRepository, drawDateGenerator, hashGenerator);
    }

}
