package strategy;

public class DistanceAttack implements AttackStrategy{
    @Override
    public void attack(String attacker,String target) {
    }
    public int modifyDamage(int baseDamage) {
        if(Math.random()<0.15){
            return 0;
        }
        return (int )(baseDamage * 1.2);
    }
}
