package persons;

import strategy.AttackStrategy;

public class Mage extends Hero {
    private boolean shieldActive = false;

    public Mage(String name, int health, int strength, AttackStrategy attackStrategy) {
        super(name, health, strength, attackStrategy);
    }

    @Override
    public void useUltimate(Hero target, String choice) {
        if (ultimateUsed) return;
        ultimateUsed = true;

        switch (choice.trim().toLowerCase()) {
            case "fire storm" -> {
                int dmg = 20 + (int)(Math.random() * 10);
                target.receiveDamage(dmg);
            }
            case "arcane shield" -> {
                shieldActive = true;
            }
        }
    }

    @Override
    public void receiveDamage(int dmg) {
        if (shieldActive) {
            shieldActive = false;
            return;
        }
        super.receiveDamage(dmg);
    }
}
