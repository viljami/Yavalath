package com.codingame.start;

import java.util.*;
import java.io.*;
import java.math.*;

// import com.codingame.game.Player;

class Boss1 {
    public static class Action {
        public final int row;
        public final int col;
        public Boss1 player;

        public Action(Boss1 player, int row, int col) {
            this.player = player;
            this.row = row;
            this.col = col;
        }
    }

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

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        setHexagons();
        int myId = in.nextInt();

        while (true) {
            int count = in.nextInt();
            for(int i = 0; i < count; ++i) {
                String inp = in.next();
            }
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            if(opponentX > -1)
                hexagons.get(opponentY).set(opponentX, 2);

            List<Action> actions = getValidActions();
            int r = (int)(Math.random() * actions.size());

            hexagons.get(actions.get(r).col).set(actions.get(r).row, 1);
            System.out.println(actions.get(r).row + " " + actions.get(r).col);
        }
    }
}
