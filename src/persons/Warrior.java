package persons;

import strategy.AttackStrategy;

public class Warrior extends Hero {
    public Warrior(String name, int health, int strength, AttackStrategy attackStrategy) {
        super(name, health, strength, attackStrategy);
    }

    @Override
    public void useUltimate(Hero target) {
        if (ultimateUsed) {
            System.out.println(getName() + " already used their ultimate!");
            return;
        }
        ultimateUsed = true;
        System.out.println("💢 " + getName() + " activates Rage Mode! Strength doubled!");
        this.strength *= 2;
    }
}
