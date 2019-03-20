package com.codingame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexGrid {
    List<List<Integer>> hexagons = new ArrayList<>();

    public HexGrid(){
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
    }

    public List<Action> getValidActions() {
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

    public List<Action> CheckNeighbours(Action action) {
        if(action == null)
            return null;
        List<Action> returningNeighbours = new ArrayList<>();
        List<Action> neighbours = new ArrayList<>();

        // Check horizontal
        // Check Right
        neighbours.add(action);
        for(int i = 1; i < 4; ++i) {
            if (action.row + i < hexagons.get(action.col).size() && hexagons.get(action.col).get(action.row + i) == hexagons.get(action.col).get(action.row)) {
                neighbours.add(new Action(action.player, action.row + i, action.col));
            } else {
                break;
            }
        }
        // Check left
        for(int i = 1; i < 4; ++i) {
            if (action.row - i >= 0 && hexagons.get(action.col).get(action.row - i) == hexagons.get(action.col).get(action.row)) {
                neighbours.add(new Action(action.player, action.row - i, action.col));
            } else {
                break;
            }
        }
        if(neighbours.size() > returningNeighbours.size()) {
            returningNeighbours = new ArrayList<>(neighbours);
        }

        // Check right diagonals
        // Check diagonal down right
        neighbours.clear();
        neighbours.add(action);
        int x = action.row;
        int y = action.col;
        for (int i = 0; i < 3; ++i) {
            ++y;
            if(y <= 4)
                ++x;

            if(x + y < 13 && y < 9 && hexagons.get(y).get(x) == hexagons.get(action.col).get(action.row)) {
                neighbours.add(new Action(action.player, x, y));
            } else {
                break;
            }
        }

        // Check diagonal up left
        x = action.row;
        y = action.col;
        for (int i = 0; i < 3; ++i) {
            --y;
            if(y < 4)
                --x;

            if(x >= 0 && y >= 0 && hexagons.get(y).get(x) == hexagons.get(action.col).get(action.row)) {
                neighbours.add(new Action(action.player, x, y));
            } else {
                break;
            }
        }
        if(neighbours.size() > returningNeighbours.size()) {
            returningNeighbours = new ArrayList<>(neighbours);
        }

        // Check left diagonals
        // Check diagonal down left
        neighbours.clear();
        neighbours.add(action);
        x = action.row;
        y = action.col;
        for (int i = 0; i < 3; ++i) {
            ++y;
            if(y > 4)
                --x;

            if(x >= 0 && y <9 && hexagons.get(y).get(x) == hexagons.get(action.col).get(action.row)) {
                neighbours.add(new Action(action.player, x, y));
            } else {
                break;
            }
        }

        // Check diagonal up right
        x = action.row;
        y = action.col;
        for(int i = 0; i < 3; ++i) {
            --y;
            if(y >= 4)
                ++x;

            if(y >= 0 && x < hexagons.get(y).size() && hexagons.get(y).get(x) == hexagons.get(action.col).get(action.row)) {
                neighbours.add(new Action(action.player, x, y));
            } else {
                break;
            }
        }
        if(neighbours.size() > returningNeighbours.size()) {
            returningNeighbours = new ArrayList<>(neighbours);
        }

        return returningNeighbours;
    }

    public int MakeAction(Action action, Player player) {
        hexagons.get(action.col).set(action.row, (player.getIndex() + 1));
        CheckNeighbours(action);
        return CheckNeighbours(action).size();
    }
}
