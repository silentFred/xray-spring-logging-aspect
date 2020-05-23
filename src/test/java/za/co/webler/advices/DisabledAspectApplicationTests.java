package za.co.webler.advices;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DisabledAspectApplicationTests {

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Mock
    private Appender mockAppender;

    private WasteTimeTestService wasteTimeService;

    @Before
    public void setUp() {
        XRayPerformanceAspect XRayPerformanceAspect = new XRayPerformanceAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new WasteTimeTestService());
        XRayPerformanceAspect.setDisabled(true);
        factory.addAspect(XRayPerformanceAspect);
        wasteTimeService = factory.getProxy();

        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

    }

    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void should_test_custom_annotation_with_value_parameter_and_method_without_parameters() throws InterruptedException {
        wasteTimeService.timeWasteOne();
        verify(mockAppender, never()).doAppend(captorLoggingEvent.capture());
    }

}
