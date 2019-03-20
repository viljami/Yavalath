import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;
public class Main {
    public static void main(String[] args) {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(Agent2.class);

        gameRunner.start();
        /*
        for (int i = 0; i < 1000; ++i) {
            MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
            gameRunner.setSeed((long) (i * 100));
            gameRunner.addAgent(Agent1.class);
            gameRunner.addAgent(Agent2.class);
            GameResult result = gameRunner.simulate();
        }
        */
    }
}
