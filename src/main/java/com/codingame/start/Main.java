package com.codingame.start;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent(Boss1.class);
        gameRunner.addAgent(Boss1.class);

        gameRunner.start();
    }
}
