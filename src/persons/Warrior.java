package persons;

import strategy.AttackStrategy;

public class Warrior extends Hero {
    public Warrior(String name, int health, int strength, AttackStrategy attackStrategy) {
        super(name, health, strength, attackStrategy);
    }

    @Override
    public void useUltimate(Hero target, String choice) {
        if (ultimateUsed) return;
        ultimateUsed = true;

        switch (choice.trim().toLowerCase()) {
            case "rage mode" -> {
                this.strength *= 2;
            }
            case "ground slam" -> {
                int dmg = 25 + (int)(Math.random() * 10);
                target.receiveDamage(dmg);
            }
        }
    }
}
