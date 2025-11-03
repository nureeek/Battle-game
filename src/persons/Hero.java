package persons;

import observer.HeroObserver;
import strategy.AttackStrategy;
import strategy.DistanceAttack;
import strategy.MagicAttack;
import strategy.MeleeAttack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hero {
    protected String name;
    protected int health;
    protected int strength;
    protected AttackStrategy attackStrategy;
    private List<HeroObserver> observers;
    protected boolean ultimateUsed=false;
    protected boolean shielded=false;

    public Hero(String name, int health, int strength, AttackStrategy attackStrategy) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.attackStrategy = attackStrategy;
        this.observers = new ArrayList<>();
    }
    private static final Random random = new Random();


    public AttackStrategy getAttackStrategy(){
        return attackStrategy;
    }
    public int getStrength(){
        return strength;
    }

    public void changeStrategy(AttackStrategy newStrategy) {
        this.attackStrategy = newStrategy;
        notifyObservers(name + " changed attack stategy. ");

    }


    public void receiveDamage(int damage) {
        if(shielded){
            shielded=false;
            return;
        }
      if(evadeOrBlock()){
          return;
      }
      this.health -= damage;
      if(this.health <= 0){
          this.health = 0;
          notifyObservers(name+ "received "+ damage + "damage. Healthh now: " + health );
      }

    }
    private boolean evadeOrBlock(){
        double chance= Math.random();

        return switch (name ){
            case "Warrior"  -> chance < 0.2;
            case "Mage" -> {
                if (chance < 0.2) {
                    this.health += 5;
                    yield true;
                } else yield false;
            }
            case "Archer" -> chance < 0.15;
            default -> false;
        };
    }



    public void receiveStrength(int strength) {
        this.strength += strength;
        if (this.strength < 0) {
            this.strength = 0;
        }
        int maxStrength=100;
        if (this.strength>maxStrength) {
            this.strength=maxStrength;
        }
    }
    public void activateShield() {
        this.shielded = true;
        notifyObservers(name + " is now shielded!");
    }

    public boolean isShielded() {
        return shielded;
    }
    public void useUltimate(Hero target, String choice) {}


    public int attack(Hero target){
        int baseDamage=(int)((this.strength/5.0)+random.nextInt(6));
        int finalDamage=baseDamage;
        if (attackStrategy instanceof MeleeAttack melee) finalDamage = melee.modifyDamage(baseDamage);
        else if (attackStrategy instanceof DistanceAttack dist) finalDamage = dist.modifyDamage(baseDamage);
        else if (attackStrategy instanceof MagicAttack magic) finalDamage = magic.modifyDamage(baseDamage);
        attackStrategy.attack(name, target.name);
        target.receiveDamage(baseDamage);

        notifyObservers(name + " dealt " + baseDamage + " damage to " + target.getName());
        return baseDamage;

    }
    public void registerObserver(HeroObserver observer){
        observers.add(observer);
    }
    public void unregisterObserver(HeroObserver observer){
        observers.remove(observer);
    }
    protected void notifyObservers(String message) {
        for (HeroObserver observer : observers) {
            observer.update(message);
        }
    }
    public String getName() {
        return name;
    }
    public int getHealth() {
        return health;
    }
}
