import com.codingame.game.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Agent1 {
    public static List<List<Integer>> hexagons = new ArrayList<>();

    public static void setHexagons() {
        int counter = 5;
        for(int h = 0; h < 9; ++h) {
            for(int c = 0; c < counter; ++c) {
                hexagons.add(h, new ArrayList<>(Collections.nCopies(counter, 0)));
            }
            if(h < 4) {
                counter++;
            } else {
                counter--;
            }
        }
    };

    public static List<Action> getValidActions() {
        List<Action> validActions;
        validActions = new ArrayList<>();
        for(int h = 0; h < 9; ++h) {
            for(int c = 0; c < hexagons.get(h).size(); ++c) {
                if(hexagons.get(h).get(c) == 0) {
                    validActions.add(new Action(null, c, h));
                }
            }
        }

        return validActions;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        setHexagons();

        int myId = scanner.nextInt();
        while (true) {
            int count = scanner.nextInt();
            for(int i = 0; i < count; ++i) {
                String inp = scanner.next();
                System.err.println(inp);
            }

            int opponentX = scanner.nextInt();
            int opponentY = scanner.nextInt();
            if(opponentX > -1)
                hexagons.get(opponentY).set(opponentX, 2);

            List<Action> actions = getValidActions();
            int r = (int)(Math.random() * actions.size());

            hexagons.get(actions.get(r).col).set(actions.get(r).row, 1);
            System.out.println(actions.get(r).row + " " + actions.get(r).col);
        }
    }
}