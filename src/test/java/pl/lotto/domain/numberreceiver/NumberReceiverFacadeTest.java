package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    private Clock clock = Clock.systemUTC();
    private HashGenerable hashGenerator;
    private NumberReceiverFacade numberReceiverFacade;
    private LocalDateTime nextDrawDate;

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        hashGenerator = new HashGeneratorTestImpl();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        nextDrawDate = drawDateGenerator.getNextDrawDate();
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();
        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.message);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        //given
        hashGenerator = new HashGeneratorTestImpl();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        nextDrawDate = drawDateGenerator.getNextDrawDate();
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);
        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.message);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        //given
        hashGenerator = new HashGeneratorTestImpl();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        nextDrawDate = drawDateGenerator.getNextDrawDate();
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.message);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_number_out_of_range_1_99() {
        //given
        hashGenerator = new HashGeneratorTestImpl();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        nextDrawDate = drawDateGenerator.getNextDrawDate();
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, -6);
        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.message);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_correct_hash() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        String response = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().hash();
        //then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(36);
    }

    @Test
    public void should_return_correct_draw_date() {
        //given
        clock = Clock.fixed(LocalDateTime.of(2024, 8, 24, 11, 0, 0).atZone(ZoneId.of("Europe/Warsaw")).toInstant(), ZoneId.of("Europe/Warsaw"));
        HashGenerable hashGenerator = new HashGenerator();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();
        //then
        LocalDateTime expectedResponse = LocalDateTime.of(2024,8,24,12,0,0);
        assertThat(testedDrawDate).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_next_Saturday_when_date_is_Saturday_noon() {
        //given
        clock = Clock.fixed(LocalDateTime.of(2024, 8,24,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        HashGenerable hashGenerator = new HashGenerator();
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();
        //then
        LocalDateTime expectedResponse = LocalDateTime.of(2024,8,31,12,0,0);
        assertThat(testedDrawDate).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        LocalDateTime localDateTime = LocalDateTime.of(2024, 8, 23, 10, 0, 0);
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        Instant fixedInstant = localDateTime.atZone(zoneId).toInstant();
        AdjustableClock clock= new AdjustableClock(fixedInstant, zoneId);
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto4 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        TicketDto ticketDto2 = numberReceiverResponseDto2.ticketDto();
        TicketDto ticketDto3 = numberReceiverResponseDto3.ticketDto();
        TicketDto ticketDto4 = numberReceiverResponseDto4.ticketDto();
        LocalDateTime drawDate = ticketDto1.drawDate();
        //when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate);
        //then
        assertThat(allTicketsByDate).hasSize(2);
    }

    @Test
    public void should_return_empty_list_if_there_are_no_tickets() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        clock = Clock.fixed(LocalDateTime.of(2024, 8, 24,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        LocalDateTime drawDate = LocalDateTime.now(clock);
        //when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate);
        //then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void should_return_empty_list_if_given_date_is_after_next_draw_date() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        clock = Clock.fixed(LocalDateTime.of(2024,8,23,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        numberReceiverFacade = new NumberReceiverConfig().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();
        //when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate.plusWeeks(1));
        //then
        assertThat(allTicketsByDate).isEmpty();
    }
}
