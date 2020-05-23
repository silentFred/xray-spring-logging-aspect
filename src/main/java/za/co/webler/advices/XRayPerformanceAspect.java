package za.co.webler.advices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class XRayPerformanceAspect {

    @Value("${xray.logging.disabled:false}")
    private boolean disabled;

    private static Logger logger = LoggerFactory.getLogger(XRayPerformanceAspect.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public static final String TO_STRING = "toString";

    @Pointcut("@annotation(XRayAspectProperties)")
    public void XRayAspectPropertiesDefinition(XRay XRayAspectProperties) {
    }

    @Around(value = "XRayAspectPropertiesDefinition(XRayAspectProperties)", argNames = "joinPoint,XRayAspectProperties")
    public Object logPerformance(ProceedingJoinPoint joinPoint, XRay XRayAspectProperties) throws Throwable {

        if (disabled) {
            return joinPoint.proceed();
        }

        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getCanonicalName());

        final long start = System.nanoTime();
        final Object proceed = joinPoint.proceed();
        float executionTime = System.nanoTime() - start;

        try {
            executionTime = executionTime / XRayAspectProperties.timeScale().getDivisionFactor();

            CodeSignature codeSig = (CodeSignature) joinPoint.getSignature();

            StringBuilder parameterList = getParameterList(joinPoint, XRayAspectProperties, codeSig);

            boolean hasNoParameters = XRayAspectProperties.value().trim().isEmpty();

            logger.info((hasNoParameters ? joinPoint.getSignature() : XRayAspectProperties.value())
                    + " executed in " + String.format("%.2f", executionTime) + " " + XRayAspectProperties.timeScale().getUnit() + "."
                    + " " + parameterList);

        } catch (RuntimeException e) {
            XRayPerformanceAspect.logger.error("XRayPerformanceAspect failed: " + e.getMessage());
        }

        return proceed;
    }

    private StringBuilder getParameterList(ProceedingJoinPoint joinPoint,
                                           XRay XRayAspectProperties,
                                           CodeSignature codeSig) {

        StringBuilder parameterList = new StringBuilder();
        if (XRayAspectProperties.logAllParams()) {
            for (int i = 0; i < codeSig.getParameterNames().length; i++) {
                parameterList.append(codeSig.getParameterNames()[i]).append(": ");
                appendObjectMapperValue(joinPoint, parameterList, i);
                parameterList.append("; ");
            }
        } else {
            for (int i = 0; i < XRayAspectProperties.paramsToLog().length; i++) {
                int index = XRayAspectProperties.paramsToLog()[i];
                if (index < 0 || index >= codeSig.getParameterNames().length) {
                    continue;
                }
                parameterList.append(codeSig.getParameterNames()[index]).append(": ");
                appendObjectMapperValue(joinPoint, parameterList, index);
                parameterList.append("; ");
            }
        }
        return parameterList;
    }

    private void appendObjectMapperValue(ProceedingJoinPoint joinPoint,
                                         StringBuilder paramList,
                                         int index) {

        Object[] joinPointArguments = joinPoint.getArgs();
        Object joinPointArgAtIndex = joinPointArguments[index];
        Class<?> joinPointArgumentAtIndexClass = joinPointArgAtIndex.getClass();

        if (!joinPointArgumentAtIndexClass.isPrimitive()) {
            try {
                paramList.append(joinPointArgumentAtIndexClass.getName());
                paramList.append(" ");
                paramList.append(objectMapper.writeValueAsString(joinPointArgAtIndex));
            } catch (Throwable e) {
                paramList.append(joinPointArgAtIndex.toString());
            }
        } else {
            paramList.append(joinPointArgAtIndex);
        }
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
