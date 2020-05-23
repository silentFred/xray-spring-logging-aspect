package za.co.webler.advices;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.hamcrest.CoreMatchers;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AspectApplicationTests {

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Mock
    private Appender mockAppender;

    private WasteTimeTestService wasteTimeService;

    @Before
    public void setUp() {
        XRayPerformanceAspect XRayPerformanceAspect = new XRayPerformanceAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new WasteTimeTestService());
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

        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.startsWith("The first parameter executed in"));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.endsWith(TimeScale.MILLISECONDS.getUnit() + ". "));
    }

    @Test
    public void should_test_custom_annotation_with_timescale_parameter_and_method_without_parameter() throws InterruptedException {
        wasteTimeService.timeWasteTwo();

        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.startsWith("void za.co.webler.advices.WasteTimeTestService.timeWasteTwo() executed in"));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.endsWith(TimeScale.NANOSECONDS.getUnit() + ". "));
    }

    @Test
    public void should_test_custom_annotation_with_all_parameters_and_method_without_parameter() throws InterruptedException {
        wasteTimeService.timeWasteFive();

        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.startsWith("The first parameter executed in"));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.endsWith(TimeScale.MILLISECONDS.getUnit() + ". "));
    }

    @Test
    public void should_test_custom_annotation_with_paramsToLog_all_parameter_and_method_with_parameter() throws InterruptedException {
        int someIntValue = 123456;
        String someStringValue = "Hello";
        float someFloatValue = 1.005f;

        wasteTimeService.timeWasteSix(someIntValue, someStringValue, someFloatValue);

        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.startsWith("void za.co.webler.advices.WasteTimeTestService.timeWasteSix(int,String,float) executed in"));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.endsWith(TimeScale.MILLISECONDS.getUnit() + ". someIntValue: java.lang.Integer " + someIntValue + "; someFloatValue: java.lang.Float " + someFloatValue + "; "));

    }

    @Test
    public void should_test_custom_annotation_with_paramsToLog_one_parameter_and_method_with_object_parameter() throws InterruptedException {
        int someIntValue = 123456;
        String someStringValue = "Hello";
        float someFloatValue = 1.005f;
        TestEntity testEntity = new TestEntity(someIntValue, someStringValue);

        wasteTimeService.timeWasteSeven(someIntValue, someStringValue, someFloatValue, testEntity);

        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.startsWith("void za.co.webler.advices.WasteTimeTestService.timeWasteSeven(int,String,float,Object) executed in"));
    }

    @Test
    public void should_test_custom_annotation_with_logAllParams_one_parameter_and_method_with_object_parameter() throws InterruptedException {
        int someIntValue = 123456;
        String someStringValue = "Hello";
        float someFloatValue = 1.005f;
        TestEntity testEntity = new TestEntity(someIntValue, someStringValue);

        wasteTimeService.timeWasteEight(someIntValue, someStringValue, someFloatValue, testEntity);

        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(), CoreMatchers.startsWith("void za.co.webler.advices.WasteTimeTestService.timeWasteEight(int,String,float,Object) executed in"));
    }

}
