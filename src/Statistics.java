import javax.swing.*;
import java.io.*;

public class Statistics extends JFrame {
    //Player Data.
    private int playerHits;
    private int playerMisses;

    //Computer Data.
    private int aiHits;
    private int aiMisses;

    public Statistics(int[][] statistics) {
        playerHits = statistics[0][0];
        aiHits = statistics[0][1];
        playerMisses = statistics[1][0];
        aiMisses = statistics[1][1];
    }

    //This method appends the statistics to a .obj file.
    public void appendStatisticsToFile(String filename) {
        int totalPlayerHits = playerHits;
        int totalAiHits = aiHits;
        int totalPlayerMisses = playerMisses;
        int totalAiMisses = aiMisses;

        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals("Hits")) {
                        totalPlayerHits += Integer.parseInt(parts[1]);
                        totalAiHits += Integer.parseInt(parts[2]);
                    } else if (parts[0].equals("Misses")) {
                        totalPlayerMisses += Integer.parseInt(parts[1]);
                        totalAiMisses += Integer.parseInt(parts[2]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(String.format("Hits,%d,%d\n", totalPlayerHits, totalAiHits));
            writer.write(String.format("Misses,%d,%d\n", totalPlayerMisses, totalAiMisses));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
