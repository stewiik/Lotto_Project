package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.lotto.domain.numberreceiver.ValidationResult.INPUT_SUCCESS;

@AllArgsConstructor
public class NumberReceiverFacade {

    private NumberValidator numberValidator;
    private TicketRepository ticketRepository;
    private DrawDateGenerator drawDateGenerator;
    private HashGenerable hashGenerator;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUsers) {
        List<ValidationResult> validationResultList = numberValidator.validate(numbersFromUsers);
        if (!validationResultList.isEmpty()) {
            String resultMessage = numberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        String hash = hashGenerator.getHash();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hash)
                .numbersFromUser(numbersFromUsers)
                .drawDate(drawDate)
                .build();

        Ticket savedTicket = TicketMapper.mapFromTicketDtoToTicket(generatedTicket);
        ticketRepository.save(savedTicket);
        return new NumberReceiverResponseDto(generatedTicket, INPUT_SUCCESS.message);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = retrieveNextDrawDate();
        return retrieveAllTicketsByDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketsByDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = retrieveNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(TicketMapper::mapFromTicketToTicketDto)
                .collect(Collectors.toList());
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }

    public TicketDto findByHash(String hash) {
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketMapper.mapFromTicketToTicketDto(ticket);
    }
}
