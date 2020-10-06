import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    public final static int CENTER_X = WIDTH/2;
    public final static int ROADSIDE_WIDTH = 14;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private ProgressBar progressBar;
    private int score;

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "YOU ARE WIN", Color.GREEN, 20);
        stopTurnTimer();
    }

    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You are Lose", Color.BLUE, 10);
        stopTurnTimer();
        player.stop();
    }

    public void onKeyPress(Key key) {
        if (key == Key.LEFT) {
            player.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            player.setDirection(Direction.RIGHT);
        }
        if (key == Key.SPACE && isGameStopped == true) {
            createGame();
        }
        if (key == Key.UP) {
            player.speed = 2;
        }
    }

    public void onKeyReleased(Key key) {
        if (key == Key.LEFT && player.getDirection() == Direction.LEFT) {
            player.setDirection(Direction.NONE);
        } else if (key == Key.RIGHT && player.getDirection() == Direction.RIGHT) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.UP) {
            player.speed = 1;
        }
    }

    private void moveAll(){
        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }

    public void onTurn(int x) {
        score = score - 5;
        setScore(score);
        if (roadManager.checkCrush(player)) {
            gameOver();
            drawScene();
            return;
        }
        if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
            finishLine.show();
        }
        if (finishLine.isCrossed(player)) {
            win();
            drawScene();
            return;
        }
        moveAll();
        roadManager.generateNewRoadObjects(this);
        drawScene();
    }

    public void setCellColor(int x, int y, Color color){
        if (x < 0 || x >= WIDTH) {
            return;
        }
        if (y < 0 || y >= HEIGHT) {
            return;
        }

        super.setCellColor(x, y, color);
    }

    private void createGame() {
        score = 3500;
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        setTurnTimer(40);
        roadManager = new RoadManager();
        isGameStopped = false;
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        drawScene();
    }
    private void drawScene(){
        drawField();
        roadMarking.draw(this);
        player.draw(this);
        roadManager.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
    }

    private void drawField(){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < ROADSIDE_WIDTH; x ++) {
                setCellColor(x, y, Color.GREEN);
            }
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = WIDTH - ROADSIDE_WIDTH; x < WIDTH; x ++) {
                setCellColor(x, y, Color.GREEN);
            }
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = ROADSIDE_WIDTH; x < WIDTH - ROADSIDE_WIDTH; x ++) {
                setCellColor(x, y, Color.GREY);
                if (x == CENTER_X) {
                    setCellColor(x, y, Color.WHITE);
                }
            }
        }

    }





}
