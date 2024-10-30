package tank1990.main;

public class GameState {
    private static GameState instance;
    private boolean isSoundOn;
    private GameState(){
        this.isSoundOn= true;
    }
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    public boolean isSoundOn(){
        return isSoundOn;
    }
    public void setSoundOn(boolean soundOn) {
        this.isSoundOn = soundOn;
    }

}
