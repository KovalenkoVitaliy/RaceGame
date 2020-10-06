package road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
   public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;

    public int getPassedCarsCount(){
        return passedCarsCount;
    }

    private boolean isRoadSpaceFree(RoadObject object){
        for (RoadObject element : items) {
            if (element.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
                return false;
            }
        }
        return true;
    }

    private  boolean isMovingCarExists() {
        for (RoadObject element : items) {
            if (element instanceof MovingCar) {
                return true;
            }
        }
        return false;
    }

    private void generateMovingCar(Game game) {
        int choise = game.getRandomNumber(100);
        if (choise < 10 && !isMovingCarExists()) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        if (type ==RoadObjectType.THORN) {
            return new Thorn(x,y);
        } else if (type ==RoadObjectType.DRUNK_CAR) {
            return new MovingCar(x,y);
        } else {
            return new Car(type, x, y);
        }
    }

    private void generateRegularCar(Game game){
        int choise = game.getRandomNumber(100);
        int carTypeNumber = game.getRandomNumber(4);
        if (choise < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    public boolean checkCrush(PlayerCar playerCar) {
        for (RoadObject el : items) {
            if (el.isCollision(playerCar)) {
                return true;
            }
        }
        return false;
    }

    private void deletePassedItems() {
        Iterator<RoadObject> iterator = items.iterator();
        while (iterator.hasNext()) {
            RoadObject el = iterator.next();
            if (el.y >= RacerGame.HEIGHT) {
                if (!(el instanceof Thorn)) {
                    passedCarsCount++;
                }
                iterator.remove();
            }
        }
        /*
        for (RoadObject el : items) {
            if (el.y > RacerGame.HEIGHT) {
                items.remove(el);
            }
        }
         */
    }

    private boolean isThornExists() {
        for (RoadObject elem: items) {
            if (elem instanceof Thorn) {
                return true;
            }
        }
        return false;
    }

    private void generateThorn(Game game){
        int ver = game.getRandomNumber(100);
        if (ver < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    public void generateNewRoadObjects(Game game){
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private  void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject ob = createRoadObject(type, x, y);
        if (isRoadSpaceFree(ob)) {
            if (ob != null) {
                items.add(ob);
            }
        }
    }

    public void draw(Game game) {
        for (RoadObject x : items) {
            x.draw(game);
        }
    }

    public void move(int boost) {
        for (RoadObject x : items) {
            x.move(boost + x.speed, items);
        }
        deletePassedItems();
    }

}
