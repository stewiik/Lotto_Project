package pl.lotto.domain.numbergenerator;

public class WinningNumbersNotFoundException extends RuntimeException {

    public WinningNumbersNotFoundException(String message) {
        super(message);
    }
}
