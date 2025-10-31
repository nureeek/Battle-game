package strategy;

public class DistanceAttack implements AttackStrategy{
    @Override
    public void attack(String attacker,String target) {
        System.out.println(attacker+" attacks by shoots "+target);
    }
}
