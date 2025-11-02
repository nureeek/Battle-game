package ui;

import decorator.DefenseBoost;
import factory.HeroFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persons.Hero;


public class GameApp extends Application {
    private Hero hero1;
    private Hero hero2;
    private TextArea logArea;
    @Override
    public void start(Stage stage){
            stage.setTitle("Hero battle game");
            VBox root = new VBox();
            root.setPadding(new Insets(15));

            logArea = new TextArea();
            logArea.setEditable(false);
            logArea.setPrefHeight(250);

        Button attackBtn=new Button("Attack");
        Button ultimateBtn=new Button("Ultimate");
        Button defenseBtn=new Button("Defense");
        Button statsBtn=new Button("Stats");
        Button newGameBtn=new Button("New Game");
        Button exitBtn=new Button("Exit");
        HBox buttonBox=new HBox(10,attackBtn,ultimateBtn,defenseBtn,statsBtn,newGameBtn,exitBtn);

        hero1= HeroFactory.createWarrior();
        hero2= HeroFactory.createMage();
        logArea.appendText("Battle started between " + hero1.getName()+  " and " + hero2.getName() + "\n");

        attackBtn.setOnAction(e -> {
            new Thread(() -> {
                hero1.attack(hero2);
                hero2.attack(hero1);

                javafx.application.Platform.runLater(() -> {
                    logArea.appendText(hero1.getName() + " attacked " + hero2.getName() + "\n");
                    logArea.appendText(hero2.getName() + " attacked " + hero1.getName() + "\n");
                    checkHealth();
                });
            }).start();
        });

        ultimateBtn.setOnAction(e -> {
            hero1.useUltimate(hero2);
            logArea.appendText(hero1.getName() + " used Ultimate!\n");
            checkHealth();
        });

        defenseBtn.setOnAction(e -> {
            hero1 = new DefenseBoost(hero1);
            logArea.appendText(hero1.getName() + " activated Defense Boost!\n");
        });
        ultimateBtn.setOnAction(e -> {
            javafx.application.Platform.runLater(() -> {
                ChoiceDialog<String> dialog = new ChoiceDialog<>("Fire Storm", "Fire Storm", "Arcane Shield");
                dialog.setTitle("Ultimate Choice");
                dialog.setHeaderText("Choose Ultimate Ability for " + hero1.getName());
                dialog.setContentText("Select ability:");

                dialog.showAndWait().ifPresent(choice -> {
                    switch (choice) {
                        case "Fire Storm" -> {
                            hero1.useUltimate(hero2);
                            logArea.appendText(hero1.getName() + " used Fire Storm! 🔥\n");
                        }
                        case "Arcane Shield" -> {
                            logArea.appendText(hero1.getName() + " used Arcane Shield! 🛡️\n");
                        }
                        default -> logArea.appendText("No ultimate used.\n");
                    }
                    checkHealth();
                });
            });
        });
        Button strategyBtn = new Button("Change Strategy");
        buttonBox.getChildren().add(strategyBtn);

        strategyBtn.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Melee", "Melee", "Ranged", "Magic");
            dialog.setTitle("Change Attack Strategy");
            dialog.setHeaderText("Select new strategy for " + hero1.getName());
            dialog.setContentText("Choose:");

            dialog.showAndWait().ifPresent(choice -> {
                switch (choice) {
                    case "Melee" -> hero1.changeStrategy(new strategy.MeleeAttack());
                    case "Ranged" -> hero1.changeStrategy(new strategy.DistanceAttack());
                    case "Magic" -> hero1.changeStrategy(new strategy.MagicAttack());
                }
                logArea.appendText(hero1.getName() + " changed attack strategy to " + choice + " ⚔️\n");
            });
        });


        statsBtn.setOnAction(e -> showStats());
        newGameBtn.setOnAction(e -> startNewBattle());

        root.getChildren().addAll(buttonBox, logArea);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    private void startNewBattle(){
        hero1=HeroFactory.createWarrior();
        hero2=HeroFactory.createMage();
        logArea.clear();
        logArea.appendText("New Battle started between " + hero1.getName()+ " and " + hero2.getName()+"\n");

        }
    private void showStats(){
        logArea.appendText(String.format("\n📊 Stats:\n%s ❤️ %d | 💪 %d\n%s ❤️ %d | 💪 %d\n\n",
                hero1.getName(), hero1.getHealth(), hero1.getStrength(),
                hero2.getName(), hero2.getHealth(), hero2.getStrength()));
    }
    private void checkHealth(){
        if (hero1.getHealth()<=0 || hero2.getHealth()<=0){
            String winner=hero1.getHealth()> hero2.getHealth() ? hero1.getName() : hero2.getName();
            logArea.appendText("🏁 Battle over! Winner: " + winner + "\n");
        }

    }

    public static void main(String[] args) {
        launch();
    }

}
