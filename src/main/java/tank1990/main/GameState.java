package tank1990.main;

public class GameState {
    private static GameState instance;
    private static boolean isSoundOn;
    private boolean isFullScreen;

    private GameState(){
        this.isSoundOn= true;
        this.isFullScreen=false;
    }
    public static GameState getInstance(){
        if (instance == null){
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

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.isFullScreen = fullScreen;
    }
}
