package pl.lotto.domain.resultannouncer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ResultResponseRepositoryTestImpl implements ResultResponseRepository {

    private final Map<String, ResultResponse> resultResponseList = new ConcurrentHashMap<>();

    @Override
    public boolean existsById(String hash) {
        return resultResponseList.containsKey(hash);
    }

    @Override
    public Optional<ResultResponse> findById(String hash) {
        return Optional.ofNullable(resultResponseList.get(hash));
    }

    @Override
    public ResultResponse save(ResultResponse resultResponse) {
        return resultResponseList.put(resultResponse.hash(), resultResponse);
    }
}
