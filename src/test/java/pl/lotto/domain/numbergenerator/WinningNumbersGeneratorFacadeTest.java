package pl.lotto.domain.numbergenerator;

import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WinningNumbersGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);


    @Test
    public void should_return_set_of_required_size() {
        //given
        RandomNumbersGenerable generator = new WinningNumbersGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        assertThat(generatedNumbers.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_within_required_range() {
        //given
        RandomNumbersGenerable generator = new WinningNumbersGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGeneratorFacade = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGeneratorFacade.generateWinningNumbers();
        //then
        int upperBound = 99;
        int lowerBound = 1;
        Set<Integer> winningNumbers = generatedNumbers.winningNumbers();
        boolean numberInRange = winningNumbers.stream().allMatch(number -> number >= lowerBound && number <= upperBound);
        assertThat(numberInRange).isTrue();
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        Set<Integer> numbers = Set.of(1, 4, 6, 56, 34, 100);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImp(numbers);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        //then
        assertThrows(IllegalStateException.class, numbersGenerator::generateWinningNumbers, "Number out of range");
    }

    @Test
    public void should_return_collection_of_unique_values() {
        //given
        RandomNumbersGenerable generator = new WinningNumbersGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        Set<Integer> uniqueNumbers = new HashSet<>(generatedNumbers.winningNumbers());
        assertThat(uniqueNumbers.size()).isEqualTo(generatedNumbers.winningNumbers().size());
    }

    @Test
    public void should_return_winning_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1,2,3,4,5,6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .date(drawDate)
                .id(id)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImp();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto winningNumbersDto = numbersGenerator.generateWinningNumbers();
        //then
        WinningNumbersDto expectedWinningNumbers = WinningNumbersDto.builder()
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        assertThat(expectedWinningNumbers).isEqualTo(winningNumbersDto);
    }

    @Test
    public void should_throw_an_exception_when_fail_to_retrieve_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImp();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        //then
        assertThrows(WinningNumbersNotFoundException.class, () -> numbersGenerator.receiveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_return_true_if_numbers_are_generated_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 8, 24, 12, 0, 0);
        Set<Integer> generatedNumbers = Set.of(1,2,3,4,5,6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .winningNumbers(generatedNumbers)
                .date(drawDate)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImp();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfig().createForTests(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);
    }
}