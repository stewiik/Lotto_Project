package pl.lotto.http.numbergenerator;

import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbergenerator.RandomNumbersGenerable;
import pl.lotto.infrastructure.numbergenerator.http.RandomGeneratorClientConfig;

public class RandomNumbersGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {

    public RandomNumbersGenerable remoteNumberGeneratorClient(int port, int connectionTimeout, int readTimeout) {
        RestTemplate restTemplate = restTemplate(connectionTimeout, readTimeout, restTemplateResponseErrorHandler());
        return remoteNumberGeneratorClient(restTemplate, "http://localhost", port);
    }
}
