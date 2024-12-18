package pl.lotto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.domain.AdjustableClock;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@DirtiesContext
@SpringBootTest(classes = {LottoSpringBootApplication.class, IntegrationConfig.class})
@ActiveProfiles("integration") // spring use a profile with this name - application-integration
@AutoConfigureMockMvc // allow to make a client that queries the db
@Testcontainers // allow to start the db for testing on docker
public class BaseIntegrationTest {

    public static final String WIRE_MOCK_HOST = "http://localhost";

    @Autowired
    public AdjustableClock clock;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Container
    public static final MongoDBContainer monoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @RegisterExtension // mini server for integration tests
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", monoDBContainer::getReplicaSetUrl);
        registry.add("lotto.number-generator.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("lotto.number-generator.http.client.config.port", () -> wireMockServer.getPort());
    }
}
