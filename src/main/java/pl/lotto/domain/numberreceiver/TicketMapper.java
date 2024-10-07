package pl.lotto.domain.numberreceiver;

import pl.lotto.domain.numberreceiver.dto.TicketDto;

public class TicketMapper {

    public static TicketDto mapFromTicketToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .numbersFromUser(ticket.numbersFromUser())
                .hash(ticket.hash())
                .drawDate(ticket.drawDate())
                .build();
    }

    public static Ticket mapFromTicketDtoToTicket(TicketDto ticketDto) {
        return Ticket.builder()
                .hash(ticketDto.hash())
                .numbersFromUser(ticketDto.numbersFromUser())
                .drawDate(ticketDto.drawDate())
                .build();
    }
}
