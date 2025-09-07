package applicazione.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DAODataConfig {
    public static String DATABASE = "casa_dei_giochi";
    public static  String USERNAME = "root";
    public static  String PASSWORD = "el@pFG2020";

    public static void load(Path configPath) throws IOException {
        List<String> lines = Files.readAllLines(configPath);

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("#")) continue;

            String[] parts = line.split("=", 2);
            if (parts.length < 2) continue;

            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                case "db.name" -> DATABASE = value;
                case "db.user" -> USERNAME = value;
                case "db.password" -> PASSWORD = value;
            }
        }
    }

}
