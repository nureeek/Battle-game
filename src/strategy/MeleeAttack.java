package strategy;

public class MeleeAttack implements AttackStrategy{
    @Override
    public void attack(String attacker,String target) {
    }
    public int modifyDamage(int baseDamage) {
        return (int)(baseDamage * 1.0);
    }
}
