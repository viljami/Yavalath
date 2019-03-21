package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Player extends AbstractMultiplayerPlayer {
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        return 1;
    }

    private String[] output;

    public Action getAction() throws TimeoutException, NumberFormatException, IndexOutOfBoundsException  {
        output = getOutputs().get(0).split("\\s");
        return new Action(this, Integer.parseInt(output[0]), Integer.parseInt(output[1]));
    }

    public String GetActionOutput() {
        return String.join(" ", output);
    }

    public String GetMessage() {
        String message = null;

        if(output.length > 2) {
            message = Arrays.stream(output).skip(2).collect(Collectors.joining(" "));
        }

        return message;
    }
}