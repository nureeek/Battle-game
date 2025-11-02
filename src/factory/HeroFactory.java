package factory;

import persons.Hero;
import strategy.AttackStrategy;
import strategy.DistanceAttack;
import strategy.MagicAttack;
import strategy.MeleeAttack;

public class HeroFactory {
    public static Hero createWarrior(){
        AttackStrategy meleeStrategy=new MeleeAttack();
        return new Hero("Warrior",100,3,meleeStrategy);
    }
    public static Hero createArcher(){
        AttackStrategy rangedStrategy=new DistanceAttack();
        return new Hero("Archer", 80 , 5,rangedStrategy);
    }
    public static Hero createMage(){
        AttackStrategy magicStrategy=new MagicAttack();
        return new Hero("Mage",60,7,magicStrategy);
    }

}
