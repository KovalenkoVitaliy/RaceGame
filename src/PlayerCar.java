import com.javarush.games.racer.road.RoadManager;

public class PlayerCar extends GameObject {

    private static int playerCarHeight = ShapeMatrix.PLAYER.length;
    public int speed  = 1;
    private Direction direction;

    public PlayerCar(int x, int y, int[][] matrix) {

        super(x, y, matrix);
    }

    public PlayerCar() {

        super(RacerGame.WIDTH / 2 + 2, RacerGame.HEIGHT - playerCarHeight - 1, ShapeMatrix.PLAYER);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void stop() {
        matrix = ShapeMatrix.PLAYER_DEAD;
    }

    public void move(){
        if (direction == Direction.RIGHT) {
            x = x +1;
        } else if (direction == Direction.LEFT) {
            x = x -1;
        }
        if (x < RoadManager.LEFT_BORDER) {
            x = RoadManager.LEFT_BORDER;
        } else if (x > RoadManager.RIGHT_BORDER + width) {
            x = RoadManager.RIGHT_BORDER - width;
        }
    }
}
