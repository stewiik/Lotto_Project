package pl.lotto.domain.numberreceiver;

import pl.lotto.domain.numberreceiver.dto.TicketDto;

class TicketMapper {

    static TicketDto mapFromTicketToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .numbersFromUser(ticket.numbersFromUser())
                .hash(ticket.hash())
                .drawDate(ticket.drawDate())
                .build();
    }

    static Ticket mapFromTicketDtoToTicket(TicketDto ticketDto) {
        return Ticket.builder()
                .hash(ticketDto.hash())
                .numbersFromUser(ticketDto.numbersFromUser())
                .drawDate(ticketDto.drawDate())
                .build();
    }
}
