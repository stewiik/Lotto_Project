package pl.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultResponseRepository extends MongoRepository<ResultResponse, String> {


}
