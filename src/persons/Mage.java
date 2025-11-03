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
                notifyObservers(getName() + " casts Fire Storm for " + dmg + "!");
            }
            case "arcane shield" -> {
                shieldActive = true;
                notifyObservers(getName() + " activates Arcane Shield!");
            }
            default -> notifyObservers(getName() + " tried to use unknown ultimate.");

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
