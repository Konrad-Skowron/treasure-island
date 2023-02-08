package pl.skowron.tools;

import java.util.Random;

public class GenerateMap {
    public static int rows = 40;
    public static int columns = 60;

    // t - treasure, o - obstacle, e - empty field
    public static void main(String[] args) {
        DataController.clearFile();
        for (int i = 0; i < rows; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < columns; j++) {
                char c = 'e';
                double chance = new Random().nextDouble();
                if (chance < 0.04) c = 't';
                else if (chance < 0.2) c = 'o';
                line.append(c);
            }
            DataController.writeToFile(line.toString());
        }
    }
}
