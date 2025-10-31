package persons;

import strategy.AttackStrategy;

public class Mage extends Hero {
    public Mage(String name, int health, int strength, AttackStrategy attackStrategy) {
        super(name, health, strength, attackStrategy);
    }

    @Override
    public void useUltimate(Hero target) {
        if (ultimateUsed) {
            System.out.println(getName() + " already used their ultimate!");
            return;
        }
        ultimateUsed = true;
        System.out.println("🔥 " + getName() + " casts Fire Storm!");
        int damage = 20 + (int)(Math.random() * 10);
        target.receiveDamage(damage);
    }
}
