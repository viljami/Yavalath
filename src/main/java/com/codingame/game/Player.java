package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer {
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        return 1;
    }

    public Action getAction() throws TimeoutException, NumberFormatException {
        String[] output = getOutputs().get(0).split("\\s");
        return new Action(this, Integer.parseInt(output[0]), Integer.parseInt(output[1]));
    }
}