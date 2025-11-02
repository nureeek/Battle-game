package decorator;

import persons.Hero;

public class DefenseBoost extends HeroDecorator {
    public DefenseBoost(Hero decoratedHero) {
        super(decoratedHero.getName(), decoratedHero.getHealth(), decoratedHero.getStrength(), decoratedHero.getAttackStrategy(), decoratedHero);
    }
    @Override
    public void receiveDamage(int damage) {
        int reducedDamage=(int)(damage * 0.8);
        super.receiveDamage(reducedDamage);
    }
}
