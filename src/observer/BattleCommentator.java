package observer;

import java.util.Observer;

public class BattleCommentator implements HeroObserver {
    @Override
    public void update(String message){
        System.out.println("Battle commentator"+message);
    }
}
