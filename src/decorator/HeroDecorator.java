package decorator;

import persons.Hero;
import strategy.AttackStrategy;

public abstract class HeroDecorator extends Hero {
    protected Hero decoratedHero;
    public HeroDecorator(String name, int health, int strength, AttackStrategy attackStrategy,Hero decoratedHero) {
        super(name,health,strength,attackStrategy);
        this.decoratedHero = decoratedHero;
    }
    @Override
    public int attack(Hero target) {
        return decoratedHero.attack(target);
    }
    @Override
    public void receiveStrength(int strength) {
        decoratedHero.receiveStrength(strength);
    }
}
