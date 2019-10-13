package com.codingame.start;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent("python3 ../agent1.py", "Player 1");
        gameRunner.addAgent("python3 ../agent2.py", "Player 2");

        Main.saveResult(
            gameRunner.simulate(),
            "game-result.json"
        );
    }

    private static void saveResult(GameResult result, String fileName) {
      String json = new Gson().toJson(result);

      Path tmpdir = Paths.get("./data"); // .resolve("codingame");
      tmpdir.toFile(); // .mkdirs();

      if (json != null) {
        File game = tmpdir.resolve(fileName).toFile();

        try (PrintWriter out = new PrintWriter(game)) {
          out.println(json);
        } catch (IOException e) {
          throw new RuntimeException("Cannot generate the game file", e);
        }
      }
    }
}
