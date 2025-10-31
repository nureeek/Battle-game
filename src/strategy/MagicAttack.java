package strategy;

public class MagicAttack implements AttackStrategy{
    @Override
    public void attack(String attacker,String target) {
        System.out.println(attacker+" attacks by fireball "+target);
    }
}
