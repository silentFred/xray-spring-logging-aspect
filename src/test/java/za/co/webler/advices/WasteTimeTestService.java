package za.co.webler.advices;


public class WasteTimeTestService {
    @XRay(value = "The first parameter")
    public void timeWasteOne() throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(timeScale = TimeScale.NANOSECONDS)
    public void timeWasteTwo() throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(timeScale = TimeScale.MILLISECONDS)
    public void timeWasteThree() throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(timeScale = TimeScale.SECONDS)
    public void timeWasteFour(int a) throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(value = "The first parameter", timeScale = TimeScale.MILLISECONDS, paramsToLog = {0})
    public void timeWasteFive() throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(paramsToLog = {0, 10, 2, 8})
    public void timeWasteSix(int someIntValue, String someStringValue, float someFloatValue) throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(paramsToLog = {3})
    public void timeWasteSeven(int someIntValue, String someStringValue, float someFloatValue, Object someObject) throws InterruptedException {
        Thread.sleep(1000);
    }

    @XRay(logAllParams = true)
    public void timeWasteEight(int someIntValue, String someStringValue, float someFloatValue, Object someObject) throws InterruptedException {
        Thread.sleep(1000);
    }

}
