import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EnergyReader {

    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // set data time format

    public static List<EnergyRow> readEnergyRows(String csvPath) throws IOException {
        // read file path
        try (FileReader fr = new FileReader(csvPath);
             BufferedReader br = new BufferedReader(fr)) {

            ArrayList<EnergyRow> energyRows = new ArrayList<>(144085);// creating array list

            br.readLine(); // read heads
            for (String line; (line = br.readLine()) != null; ) {
                if (!line.isEmpty()) {
                    EnergyRow energyRow = parseEnergyRow(line);
                    energyRows.add(energyRow);
                }
            }

            return energyRows;
        }

    }

    private static EnergyRow parseEnergyRow(String line) { // parse value from Energy Row
        String[] values = line.split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }

        int id = Integer.parseInt(values[0]);
        LocalDateTime timestamp = LocalDateTime.parse(values[1], FORMATTER);
        int demand = Integer.parseInt(values[2]);
        double frequency = Double.parseDouble(values[3]);

        EnergyRow energyRow = new EnergyRow(id, timestamp, demand, frequency);

        double energy; //set i = 4
        for (int i = 4; i < values.length; i++) {
            energy = Double.parseDouble(values[i]);
            // if a value of energy is less than -1e6,
            // we think it's a bad value
            if (energy < -1e6) {
                energy = 0;
            }
            // the case will run one by one
            // starting from 4

            switch (i) {
                case 4:
                    energyRow.setEnergy(EnergyType.COAL, energy);
                    break;
                case 5:
                    energyRow.setEnergy(EnergyType.NUCLEAR, energy);
                    break;
                case 6:
                    energyRow.setEnergy(EnergyType.CCGT, energy);
                    break;
                case 7:
                    energyRow.setEnergy(EnergyType.WIND, energy);
                    break;
                case 8:
                    energyRow.setEnergy(EnergyType.FRENCH_ICT, energy);
                    break;
                case 9:
                    energyRow.setEnergy(EnergyType.DUTCH_ICT, energy);
                    break;
                case 10:
                    energyRow.setEnergy(EnergyType.IRISH_ICT, energy);
                    break;
                case 11:
                    energyRow.setEnergy(EnergyType.EW_ICT, energy);
                    break;
                case 12:
                    energyRow.setEnergy(EnergyType.PUMPED, energy);
                    break;
                case 13:
                    energyRow.setEnergy(EnergyType.HYDRO, energy);
                    break;
                case 14:
                    energyRow.setEnergy(EnergyType.OIL, energy);
                    break;
                case 15:
                    energyRow.setEnergy(EnergyType.OCGT, energy);
                    break;
                case 16:
                    energyRow.setEnergy(EnergyType.OTHER, energy);
                    break;
                case 17:
                    energyRow.setEnergy(EnergyType.SOLAR, energy);
                    break;
            }
        }

        return energyRow;
    }
}