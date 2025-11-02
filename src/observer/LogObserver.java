package observer;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogObserver implements HeroObserver {
    @Override
    public void update(String event) {
        try (FileWriter writer = new FileWriter("battle_log.txt", true)) {
            writer.write(LocalDateTime.now() + " — " + event + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
