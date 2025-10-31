package observer;

import observer.HeroObserver;
import java.io.FileWriter;
import java.io.IOException;

public class LogObserver implements HeroObserver {
    @Override
    public void update(String event) {
        try (FileWriter writer = new FileWriter("battle_log.txt", true)) {
            writer.write(event + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
