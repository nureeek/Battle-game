package game;

import decorator.AngerMode;
import decorator.DefenseBoost;
import factory.HeroFactory;
import persons.Hero;
import strategy.DistanceAttack;
import strategy.MagicAttack;
import strategy.MeleeAttack;
import db.DatabaseManager;
import java.util.Scanner;

public class GameConsole {
    private final Scanner scanner = new Scanner(System.in);

    public void startGame() {
        boolean playAgain = true;

        while (playAgain) {
            DatabaseManager.initDatabase();
            System.out.println("\nWelcome to Hero Battle Game!\n");

            System.out.println("Choose your heroes:");
            Hero hero1 = chooseHero("First");
            Hero hero2 = chooseHero("Second");

            System.out.println("\n Battle begins between " + hero1.getName() + " and " + hero2.getName() + "!\n");

            boolean running = true;
            while (running) {
                System.out.println("""
                        =====================
                        Choose an action:
                        1️⃣ Attack
                        2️⃣ Use Ultimate
                        3️⃣ Change Attack Strategy
                        4️⃣ Show Hero Stats
                        5️⃣ Choose mode
                        6️⃣ Exit Battle
                        =====================""");
                System.out.print("Your choice: ");
                int choice = scanner.nextInt();


                switch (choice) {
                    case 1 -> performAttack(hero1, hero2);
                    case 2 -> useUltimate(hero1, hero2);
                    case 3 -> changeStrategy(hero1);
                    case 4 -> showStats(hero1, hero2);
                    case 6 -> {
                        System.out.println("🏁 Battle ended by user!");
                        String winner;
                        if (hero1.getHealth() > hero2.getHealth()) {
                            winner = hero1.getName();
                        } else if (hero2.getHealth() > hero1.getHealth()) {
                            winner = hero2.getName();
                        } else {
                            winner = "Draw";
                        }

                        db.DatabaseManager.saveBattle(
                                hero1.getName(),
                                hero2.getName(),
                                winner,
                                hero1.getHealth(),
                                hero2.getHealth()
                        );
                        System.out.println("💾 Battle result saved to PostgreSQL!");
                        running = false;
                    }

                    case 5 -> {
                        System.out.println("""
                                ⚙️ Choose mode:
                                1️⃣ Defense Boost (take less damage)
                                2️⃣ Anger Mode (deal more damage)
                                """);
                        int mode = scanner.nextInt();
                        if (mode == 1) hero1 = new DefenseBoost(hero1);
                        else if (mode == 2) hero1 = new AngerMode(hero1);
                        System.out.println("💪 Mode applied to " + hero1.getName());
                    }

                    default -> System.out.println("❌ Invalid choice, try again!");
                }

                if (hero1.getHealth() <= 0 || hero2.getHealth() <= 0) {
                    System.out.println("\n🏁 Battle over!");
                    String winner;

                    if (hero1.getHealth() <= 0 && hero2.getHealth() <= 0) {
                        winner = "Draw";
                        System.out.println("🤝 It's a draw!");
                    } else if (hero1.getHealth() <= 0) {
                        winner = hero2.getName();
                        System.out.println(hero2.getName() + " wins!");
                    } else {
                        winner = hero1.getName();
                        System.out.println(hero1.getName() + " wins!");
                    }

                    db.DatabaseManager.saveBattle(
                            hero1.getName(),
                            hero2.getName(),
                            winner,
                            hero1.getHealth(),
                            hero2.getHealth()
                    );
                    System.out.println("💾 Battle result saved to PostgreSQL!");
                    running = false;
                }
            }

            System.out.println("\n🎮 Do you want to play again?");
            System.out.println("1️⃣ Yes   2️⃣ No");
            int answer = scanner.nextInt();
            if (answer != 1) {
                playAgain = false;
                System.out.println("Thanks for playing Hero Battle Game!");
            } else {
                System.out.println("\n🔄 Starting a new battle...\n");
            }
        }
    }

    private Hero chooseHero(String order) {
        System.out.println(order + " hero: (1 - Warrior, 2 - Mage, 3 - Archer)");
        int option = scanner.nextInt();

        return switch (option) {
            case 1 -> HeroFactory.createWarrior();
            case 2 -> HeroFactory.createMage();
            case 3 -> HeroFactory.createArcher();
            default -> {
                System.out.println("Invalid choice! Defaulting to Warrior.");
                yield HeroFactory.createWarrior();
            }
        };
    }

    private void performAttack(Hero hero1, Hero hero2) {
        System.out.println(hero1.getName() + " attacks!");
        hero1.attack(hero2);
        if (hero2.getHealth() > 0) {
            System.out.println(hero2.getName() + " counterattacks!");
            hero2.attack(hero1);
        }
    }

    private void useUltimate(Hero hero1, Hero hero2) {
        System.out.println("""
                Choose which hero should use their Ultimate:
                1️⃣ """ + hero1.getName() + """
                2️⃣ """ + hero2.getName() + """
                """);

        int userChoice = scanner.nextInt();

        Hero attacker;
        Hero target;

        if (userChoice == 1) {
            attacker = hero1;
            target = hero2;
        } else if (userChoice == 2) {
            attacker = hero2;
            target = hero1;
        } else {
            System.out.println("Invalid choice,Returning to menu.");
            return;
        }

        if (attacker.getName().equalsIgnoreCase("Mage")) {
            System.out.println("""
                    ✨ Choose Mage Ultimate:
                    1️⃣ Fire Storm (Powerful attack 🔥)
                    2️⃣ Arcane Shield (Block next attack 🧿)
                    """);

            int ultChoice = scanner.nextInt();

            switch (ultChoice) {
                case 1 -> {
                    System.out.println("🔥 Mage casts Fire Storm!");
                    int damage = 20 + (int) (Math.random() * 10);
                    target.receiveDamage(damage);
                }
                case 2 -> {
                    System.out.println("🧿 Mage activates Arcane Shield! Next attack is blocked!");
                    attacker.useUltimate(target);
                }
                default -> System.out.println("❌ Invalid choice. No ultimate used.");
            }
        } else {
            attacker.useUltimate(target);
        }
    }

    private void changeStrategy(Hero hero) {
        System.out.println("""
                ⚙️ Choose new attack strategy:
                1️⃣ Melee Attack
                2️⃣ Ranged Attack
                3️⃣ Magic Attack
                """);

        int option = scanner.nextInt();
        switch (option) {
            case 1 -> hero.changeStrategy(new MeleeAttack());
            case 2 -> hero.changeStrategy(new DistanceAttack());
            case 3 -> hero.changeStrategy(new MagicAttack());
            default -> System.out.println("Invalid choice!");
        }
    }

    private void showStats(Hero hero1, Hero hero2) {
        System.out.println("\nCurrent Hero Stats:");
        System.out.printf("%s → ❤️ %d | 💪 %d%n", hero1.getName(), hero1.getHealth(), hero1.getStrength());
        System.out.printf("%s → ❤️ %d | 💪 %d%n%n", hero2.getName(), hero2.getHealth(), hero2.getStrength());
    }
}