import java.time.LocalDateTime;
import java.util.EnumMap;

public class EnergyRow {

    private final int id;
    private final LocalDateTime timestamp;
    private final double frequency;
    private final EnumMap<EnergyType, Double> energyMap;
    private int demand;

    public EnergyRow(int id, LocalDateTime timestamp, int demand, double frequency) { // Constructor which instantiates the variables
        this.id = id;
        this.timestamp = timestamp;
        this.demand = demand;
        this.frequency = frequency;
        energyMap = new EnumMap<>(EnergyType.class);
    }

    public EnergyRow add(EnergyRow other) { // Sets the values in the Energy Rows
        EnergyRow result = new EnergyRow(0, timestamp, this.demand + other.demand, 0);

        for (EnergyType type : EnergyType.values()) {
            result.setEnergy(type, this.getEnergy(type) + other.getEnergy(type));
        }

        return result;
    }

    public boolean isSameMonth(EnergyRow other) { // Compares month values, returns true if the months are similar
        return this.timestamp.getYear() == other.timestamp.getYear()
                && this.timestamp.getMonth() == other.timestamp.getMonth();
    }

    public void setEnergy(EnergyType type, double value) {
        energyMap.put(type, value); // Adds a value into the energy map
    }

    public double getEnergy(EnergyType type) {
        return energyMap.get(type); // Returns the value corresponding to the type requested
    }

    public int getId() {
        return id; // Returns the ID value
    }

    public LocalDateTime getTimestamp() {
        return timestamp; // Returns the time stamp value
    }

    public int getDemand() {
        return demand; // Returns the demand value
    }

    public double getFrequency() {
        return frequency; // Returns the frequenncy value
    }

    public EnumMap<EnergyType, Double> getEnergyMap() {
        return energyMap; // Returns the energy map
    }

    public EnergyRow getMean(EnergyRow record, Integer meanSum) {
        record.demand = record.demand / meanSum; // Holds the mean value for the demand
        record.setEnergy(EnergyType.COAL, record.getEnergy(EnergyType.COAL) / meanSum); // Takes mean records for the energy readings
        record.setEnergy(EnergyType.NUCLEAR, record.getEnergy(EnergyType.NUCLEAR) / meanSum);
        record.setEnergy(EnergyType.CCGT, record.getEnergy(EnergyType.CCGT) / meanSum);
        record.setEnergy(EnergyType.WIND, record.getEnergy(EnergyType.WIND) / meanSum);
        record.setEnergy(EnergyType.FRENCH_ICT, record.getEnergy(EnergyType.FRENCH_ICT) / meanSum);
        record.setEnergy(EnergyType.DUTCH_ICT, record.getEnergy(EnergyType.DUTCH_ICT) / meanSum);
        record.setEnergy(EnergyType.IRISH_ICT, record.getEnergy(EnergyType.IRISH_ICT) / meanSum);
        record.setEnergy(EnergyType.EW_ICT, record.getEnergy(EnergyType.EW_ICT) / meanSum);
        record.setEnergy(EnergyType.PUMPED, record.getEnergy(EnergyType.PUMPED) / meanSum);
        record.setEnergy(EnergyType.HYDRO, record.getEnergy(EnergyType.HYDRO) / meanSum);
        record.setEnergy(EnergyType.OIL, record.getEnergy(EnergyType.OIL) / meanSum);
        record.setEnergy(EnergyType.OCGT, record.getEnergy(EnergyType.OCGT) / meanSum);
        record.setEnergy(EnergyType.OTHER, record.getEnergy(EnergyType.OTHER) / meanSum);
        record.setEnergy(EnergyType.SOLAR, record.getEnergy(EnergyType.SOLAR) / meanSum);
        return record;
    }

}
