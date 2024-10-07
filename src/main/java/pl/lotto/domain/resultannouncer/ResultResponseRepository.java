package pl.lotto.domain.resultannouncer;

import java.util.Optional;

public interface ResultResponseRepository {
    boolean existsById(String hash);

    Optional<ResultResponse> findById(String hash);

    ResultResponse save(ResultResponse resultResponse);

}
