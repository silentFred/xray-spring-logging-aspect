XRay Spring Logging Aspect

Advice to log the time it takes to execute method

@LogPerformance annotation has 4 optional parameters:
value: String - String value you want logged with execution time, like a description. Default - Method name
timeScale: enum - The timescale you want the execution time to be logged in. Options: NANOSECONDS, MILLISECONDS, SECONDS. Default - MILLISECONDS
logAllParams: boolean - Boolean value indicating if all parameters of attached method should be logged or not Default - false
paramsToLog: int[] - Array of integers corresponding to index of parameters of attached method. Said parameter names and values are logged. Default - Display 0 parameters

eg.
@LogPerformance(value = "Our most important method", timeScale = TimeScale.NANOSECONDS, paramsToLog = {0,2})
public void computationalIntensiveMethod(int someIntValue, String someStringValue, float someFloatValue) {
    //Very computational intensive business logic
}
Will log the following:
Our most important method executed in 999751296.00 ns. someIntValue: 123; someFloatValue: 12.3;

@LogPerformance(timeScale = TimeScale.MILLISECONDS, logAllParams = true)
public void computationalIntensiveMethod(int someIntValue, String someStringValue, float someFloatValue) {
    //Very computational intensive business logic
}
Will log the following:
void za.co.webler.advices.computationalIntensiveMethod(int,String,float) executed in 1012.45 ms. someIntValue: 123; someStringValue: Hello; someFloatValue: 12.3;


Package scan the following package to start using the annotation:
za.co.webler.advices