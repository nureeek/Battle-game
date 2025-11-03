package ui;

import decorator.DefenseBoost;
import factory.HeroFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import observer.LogObserver;
import persons.Hero;
import db.DatabaseManager;
import strategy.*;

public class GameApp extends Application {
    private Hero hero1, hero2;
    private TextArea logArea;
    private ProgressBar hp1, hp2;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hero battle game");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        DatabaseManager.initDatabase();

        Label title = new Label("Choose your heroes ");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ChoiceBox<String> hero1Choice = new ChoiceBox<>();
        hero1Choice.getItems().addAll("Warrior", "Archer", "Mage");
        hero1Choice.setValue("Warrior");

        ChoiceBox<String> hero2Choice = new ChoiceBox<>();
        hero2Choice.getItems().addAll("Warrior", "Archer", "Mage");
        hero2Choice.setValue("Archer");

        Button startBtn = new Button("Start");
        VBox selectBox = new VBox(10, new Label("Choose first hero"), hero1Choice,
                new Label("Choose second hero"), hero2Choice, startBtn);
        root.getChildren().addAll(title, selectBox);
        stage.setScene(new Scene(root, 300, 300));
        stage.show();

        startBtn.setOnAction(e -> {
            hero1 = createHero(hero1Choice.getValue());
            hero2 = createHero(hero2Choice.getValue());

            hero1.registerObserver(new LogObserver());
            hero2.registerObserver(new LogObserver());

            startBattle(stage);
        });
    }

    private Hero createHero(String type) {
        return switch (type) {
            case "Warrior" -> HeroFactory.createWarrior();
            case "Archer" -> HeroFactory.createArcher();
            case "Mage" -> HeroFactory.createMage();
            default -> throw new IllegalStateException("Unexpected hero: " + type);
        };
    }

    private void startBattle(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(250);
        log("Battle started between " + hero1.getName() + " and " + hero2.getName());

        hp1 = new ProgressBar(hero1.getHealth() / 100.0);
        hp2 = new ProgressBar(hero2.getHealth() / 100.0);
        HBox healthBox = new HBox(15, new Label(hero1.getName()), hp1, new Label(hero2.getName()), hp2);

        Button attack = new Button("Attack");
        Button ultimate = new Button("Ultimate");
        Button strategy = new Button("Strategy");
        Button defense = new Button("Defense");
        Button stats = new Button("Stats");
        Button newGame = new Button("New Game");
        Button exit = new Button("Exit");
        HBox buttons = new HBox(10, attack, ultimate,strategy, defense, stats, newGame, exit);

        attack.setOnAction(e -> {
            attack(hero1, hero2);
            pause(600);
            if (hero2.getHealth() > 0) attack(hero2, hero1);
            checkWinner();
        });

        defense.setOnAction(e -> {
            Hero def = chooseHero("Who use the defense ability?");
            if (def == null) return;
            switch (def.getName()) {
                case "Warrior" -> { log(def.getName() + " braces for impact!"); def = new DefenseBoost(def); }
                case "Mage" -> { log(def.getName() + " restores HP"); def.receiveDamage(-10); }
                case "Archer" -> { log(def.getName() + " evades and gains +2 strength"); def.receiveStrength(2); }
                default -> log(def.getName() + " cannot use defense.");
            }
            updateHP();
        });

        strategy.setOnAction(e -> {
            Hero selectedHero = chooseHero("Who is attack strategy do you want to change?");
            if (selectedHero == null) return;

            ChoiceDialog<String> d = new ChoiceDialog<>("Melee", "Distance", "Magic");
            d.setTitle("Change Attack Strategy");
            d.setHeaderText("Select new strategy for " + selectedHero.getName());
            d.showAndWait().ifPresent(c -> {
                switch (c) {
                    case "Melee" -> selectedHero.changeStrategy(new MeleeAttack());
                    case "Distance" -> selectedHero.changeStrategy(new DistanceAttack());
                    case "Magic" -> selectedHero.changeStrategy(new MagicAttack());
                }
                log(selectedHero.getName() + " changed attack strategy to " + c);
            });
        });


        ultimate.setOnAction(e -> {
            Hero a = chooseHero("Who uses ultimate?");
            if (a == null) return;
            Hero t = a == hero1 ? hero2 : hero1;
            ChoiceDialog<String> d = switch (a.getName()) {
                case "Warrior" -> new ChoiceDialog<>("Rage Mode", "Rage Mode", "Ground Slam");
                case "Mage" -> new ChoiceDialog<>("Fire Storm", "Fire Storm", "Arcane Shield");
                case "Archer" -> new ChoiceDialog<>("Triple Shot", "Triple Shot", "Headshot");
                default -> null;
            };
            if (d == null) { log(a.getName() + " has no ultimates."); return; }
            d.setHeaderText("Choose ultimate:");
            d.showAndWait().ifPresent(c -> {
                applyUltimate(a, t, c.trim().toLowerCase());
                updateHP();
                checkHealth();
            });
        });

        stats.setOnAction(e -> log(String.format("\nStats:\n%s %d | %d\n%s %d | %d\n",
                hero1.getName(), hero1.getHealth(), hero1.getStrength(),
                hero2.getName(), hero2.getHealth(), hero2.getStrength())));

        newGame.setOnAction(e -> {
            saveBattle();
            log("Battle saved before starting new one");
            start(stage);
        });

        exit.setOnAction(e -> {
            saveBattle();
            log("Battle result saved before exit");
            stage.close();
        });

        root.getChildren().addAll(healthBox, buttons, logArea);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void attack(Hero a, Hero t) {
        int damage=a.attack(t);
        log(a.getName() + " attacked " + t.getName()+ " and dealt " + damage+ " damage");
        updateHP();
    }

    private Hero chooseHero(String prompt) {
        ChoiceDialog<String> d = new ChoiceDialog<>(hero1.getName(), hero1.getName(), hero2.getName());
        d.setTitle("Choose hero");
        d.setHeaderText(prompt);
        return d.showAndWait().map(n -> n.equals(hero1.getName()) ? hero1 : hero2).orElse(null);
    }

    private void updateHP() {
        hp1.setProgress(hero1.getHealth() / 100.0);
        hp2.setProgress(hero2.getHealth() / 100.0);
    }

    private void log(String t) {
        logArea.appendText(t + "\n");
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private void checkWinner() {
        if (hero1.getHealth() <= 0 || hero2.getHealth() <= 0) {
            String w = hero1.getHealth() <= 0 && hero2.getHealth() <= 0 ? "Draw" :
                    (hero1.getHealth() <= 0 ? hero2.getName() : hero1.getName());
            log("\nWinner: " + w);
            saveBattle();
        }
    }

    private void checkHealth() {
        if (hero1.getHealth() <= 0 || hero2.getHealth() <= 0) {
            log("Battle over! Winner: " + (hero1.getHealth() > hero2.getHealth() ? hero1.getName() : hero2.getName()));
            saveBattle();
        }
    }

    private void saveBattle() {
        String w = hero1.getHealth() > hero2.getHealth() ? hero1.getName()
                : (hero1.getHealth() < hero2.getHealth() ? hero2.getName() : "Draw");
        DatabaseManager.saveBattle(hero1.getName(), hero2.getName(), w, hero1.getHealth(), hero2.getHealth());
    }

    private void applyUltimate(Hero a, Hero t, String c) {
        a.useUltimate(t, c);
        log(a.getName() + " used " + c);
    }
    public static void main(String[] args) {
        launch();
    }
}
