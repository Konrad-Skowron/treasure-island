package pl.skowron.tools;

import pl.skowron.logic.Field;
import pl.skowron.logic.FieldContent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataController {
    private static final Path path = Path.of("map.txt");

    public static List<String> readFromFile() {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeToFile(String content) {
        try {
            Files.writeString(path, content + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void clearFile() {
        try {
            Files.writeString(path, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<List<Field>> loadMap() {
        List<List<Field>> map = new ArrayList<>();
        List<String> mapFile = readFromFile();
        for (int i = 0; i < mapFile.size(); i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < mapFile.get(i).length(); j++) {
                if (mapFile.get(i).charAt(j) == 'e') {
                    map.get(i).add(new Field(FieldContent.EMPTY, 0, false));
                } else if (mapFile.get(i).charAt(j) == 'o') {
                    map.get(i).add(new Field(FieldContent.OBSTACLE, 0, false));
                } else if (mapFile.get(i).charAt(j) == 't') {
                    map.get(i).add(new Field(FieldContent.TREASURE, new Random().nextInt(5000), false));
                }
            }
        }
        return map;
    }
}
