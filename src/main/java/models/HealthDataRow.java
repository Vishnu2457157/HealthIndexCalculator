
package models;

public class HealthDataRow {
    public int age;
    public int pulseRate;
    public int bloodPressure;

    public HealthDataRow() {}

    public HealthDataRow(int age, int pulseRate, int bloodPressure) {
        this.age = age;
        this.pulseRate = pulseRate;
        this.bloodPressure = bloodPressure;
    }
}

