package decorator;

import persons.Hero;

public class AngerMode extends HeroDecorator{
    public AngerMode(Hero decoratedHero) {
        super(decoratedHero.getName(), decoratedHero.getHealth(), decoratedHero.getStrength(), decoratedHero.getAttackStrategy(), decoratedHero);
    }
    @Override
    public void attack(Hero target){
        int rageDamage=15;
        super.attack(target);
    }
}
