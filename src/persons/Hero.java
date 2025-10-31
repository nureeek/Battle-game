package persons;

import observer.HeroObserver;
import strategy.AttackStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hero {
    protected String name;
    protected int health;
    protected int strength;
    protected AttackStrategy attackStrategy;
    private List<HeroObserver> observers;
    protected boolean ultimateUsed=false;
    private boolean shielded=false;

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
            System.out.println(name+ "'s Arcane Shield blocks all damage!");
            shielded=false;
            return;
        }
        double chance=Math.random();
        if(name.equals("Warrior") && chance <0.2){
            System.out.println(name + " blocked the attack");
            damage=0;
        }
        else if(name.equals("Mage") && chance <0.2){
            System.out.println(name+" channels a powerful spell, reflecting part of the damage");
            damage=damage/2;
        }
        else if(name.equals("Archer")&& chance <0.15){
            System.out.println(name+ " dodged the attack gracefully!");
            damage=0;
        }
        this.health -= damage;
        if (this.health <= 0) this.health = 0;
        notifyObservers(name + " received " + damage + " damage. Health is now " + health);

    }
    public void useUltimate(Hero target) {
        if (ultimateUsed) {
            System.out.println(name + " already used their ultimate ability!");
            return;
        }

        ultimateUsed = true;

        switch (name.toLowerCase()) {

            case "warrior" -> {
                System.out.println("""
                💢 Choose Warrior Ultimate:
                1️⃣ Rage Mode (Double strength for next attacks)
                2️⃣ Ground Slam (Heavy strike dealing instant damage)
                """);

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.println("💢 " + name + " enters Rage Mode! Strength doubled!");
                    this.strength *= 2;
                } else if (choice == 2) {
                    System.out.println("💥 " + name + " performs a Ground Slam!");
                    int damage = 25 + (int)(Math.random() * 10);
                    target.receiveDamage(damage);
                } else {
                    System.out.println("❌ Invalid choice, ultimate cancelled.");
                    ultimateUsed = false;
                }
            }

            case "mage" -> {
                System.out.println("""
                ✨ Choose Mage Ultimate:
                1️⃣ Fire Storm (Powerful area attack 🔥)
                2️⃣ Arcane Shield (Block next attack 🧿)
                """);

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.println("🔥 " + name + " casts Fire Storm!");
                    int damage = 20 + (int)(Math.random() * 10);
                    target.receiveDamage(damage);
                } else if (choice == 2) {
                    System.out.println("🧿 " + name + " activates Arcane Shield!");
                    this.shielded = true;
                } else {
                    System.out.println("❌ Invalid choice, ultimate cancelled.");
                    ultimateUsed = false;
                }
            }

            case "archer" -> {
                System.out.println("""
                🏹 Choose Archer Ultimate:
                1️⃣ Triple Shot (Three rapid attacks)
                2️⃣ Headshot (High chance critical hit)
                """);

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.println("🏹 " + name + " uses Triple Shot!");
                    for (int i = 0; i < 3; i++) attack(target);
                } else if (choice == 2) {
                    System.out.println("🎯 " + name + " aims carefully... HEADSHOT!");
                    int damage = 30 + (int)(Math.random() * 10);
                    target.receiveDamage(damage);
                } else {
                    System.out.println("❌ Invalid choice, ultimate cancelled.");
                    ultimateUsed = false;
                }
            }

            default -> System.out.println(name + " has no special ultimate ability.");
        }
    }

    public void autoHeal() {
        if (this.health <= 20) {
            int healAmount = 30;
            this.health += healAmount;
            System.out.println(name + " uses a Healing Potion and restores " + healAmount + " HP!");
        }
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
        System.out.println(name + "now has "+this.strength+" strength");
    }

    public void attack(Hero target){
        int baseDamage=(this.strength/5)+random.nextInt(6);
        attackStrategy.attack(name, target.name);
        target.receiveDamage(baseDamage);

        notifyObservers(name + " dealt " + baseDamage + " damage to " + target.getName());

    }
    public void registerObserver(HeroObserver observer){
        observers.add(observer);
    }
    public void unregisterObserver(HeroObserver observer){
        observers.remove(observer);
    }
    private void notifyObservers(String message) {
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
