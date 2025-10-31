package strategy;

public class MeleeAttack implements AttackStrategy{
    @Override
    public void attack(String attacker,String target) {
        System.out.println(attacker+" attacks by sword "+target);
    }
}
