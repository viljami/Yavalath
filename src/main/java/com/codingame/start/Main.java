package com.codingame.start;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent("python3 ../agent1.py", "Player 1");
        gameRunner.addAgent("python3 ../agent2.py", "Player 2");

        gameRunner.start();
    }
}
