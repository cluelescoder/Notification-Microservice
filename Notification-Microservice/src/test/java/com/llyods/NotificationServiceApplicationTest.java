package com.llyods;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

@SpringBootTest
class NotificationServiceApplicationTest {

    @Test
    void mainMethodRunsApplication() {
        try (var mockSpringApplication = mockStatic(SpringApplication.class)) {
            NotificationServiceApplication.main(new String[]{});
            mockSpringApplication.verify(() -> SpringApplication.run(NotificationServiceApplication.class, new String[]{}));
        }
    }
}
