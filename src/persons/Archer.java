package persons;

import strategy.AttackStrategy;

public class Archer extends Hero {
    public Archer(String name, int health, int strength, AttackStrategy attackStrategy) {
        super(name, health, strength, attackStrategy);
    }

    @Override
    public void useUltimate(Hero target, String choice) {
        if (ultimateUsed) return;
        ultimateUsed = true;

        switch (choice.trim().toLowerCase()) {
            case "triple shot" -> {
                int total = 0;
                for (int i = 0; i < 3; i++) {
                    int shot = 5 + (int)(Math.random() * 5);
                    target.receiveDamage(shot);
                    total += shot;
                }
            }
            case "headshot" -> {
                int dmg = 30 + (int)(Math.random() * 10);
                target.receiveDamage(dmg);
            }
        }
    }
}
