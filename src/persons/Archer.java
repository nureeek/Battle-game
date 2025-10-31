package persons;

import strategy.AttackStrategy;

public class Archer extends Hero {
    public Archer(String name, int health, int strength, AttackStrategy attackStrategy) {
        super(name, health, strength, attackStrategy);
    }

    @Override
    public void useUltimate(Hero target) {
        if (ultimateUsed) {
            System.out.println(getName() + " already used their ultimate!");
            return;
        }
        ultimateUsed = true;
        System.out.println("🏹 " + getName() + " performs Triple Shot!");
        for (int i = 0; i < 3; i++) {
            attack(target);
        }
    }
}
