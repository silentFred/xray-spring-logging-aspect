package za.co.webler.advices;

public enum TimeScale {
    NANOSECONDS("ns", 1), MILLISECONDS("ms", 1000_000), SECONDS("s", 1000_000_000);

    private String unit;
    private Integer divisionFactor;

    TimeScale(String unit, int divisionFactor) {
        this.unit = unit;
        this.divisionFactor = divisionFactor;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getDivisionFactor() {
        return divisionFactor;
    }
}
