package decorator;

import persons.Hero;

public class AngerMode extends HeroDecorator{
    public AngerMode(Hero decoratedHero) {
        super(decoratedHero.getName(), decoratedHero.getHealth(), decoratedHero.getStrength(), decoratedHero.getAttackStrategy(), decoratedHero);
    }
    @Override
    public int attack(Hero target){
        int baseDamage=super.attack(target);
        int rageDamage=15;
        int totalDamage=baseDamage+rageDamage;
        target.receiveDamage(rageDamage);
        notifyObservers(getName()+ " is Anger mode,extra damage: "+ rageDamage);
        return totalDamage;
    }
}
