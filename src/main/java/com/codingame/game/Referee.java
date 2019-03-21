package com.codingame.game;
import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import view.View;
import view.toggle.ToggleModule;

public class Referee extends AbstractReferee {

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    @Inject private TooltipModule tooltips;
    @Inject private ToggleModule toggleModule;
    @Inject private EndScreenModule endScreenModule;

    private Action lastAction = null;
    private View view;
    private HexGrid hexGrid;

    @Override
    public void init() {
        gameManager.setTurnMaxTime(100);
        view = new View(graphicEntityModule, tooltips, toggleModule, gameManager.getPlayers());
        hexGrid = new HexGrid();
    }

    @Override
    public void gameTurn(int turn) {
        Player player = gameManager.getPlayer(turn % gameManager.getPlayerCount());
        sendInputs(player, turn);
        player.execute();
        try {
            final Action action = player.getAction();
            List<Action> validActions = hexGrid.getValidActions();

            view.DrawMessage(graphicEntityModule, player);

            if(turn == 1)
                validActions.add(lastAction);

            if(!validActions.contains(action)) {
                if(action.row >= 0 && action.col >= 0 && action.col <= 8 && action.row < hexGrid.hexagons.get(action.col).size()) {
                    throw new InvalidAction(String.format("Player %s played an illegal action (%d %d). Cell was occupied.", action.player.getNicknameToken(), action.row, action.col));
                } else {
                    throw new InvalidAction(String.format("Player %s played an illegal action (%d %d). Out of bounds move.", action.player.getNicknameToken(), action.row, action.col));
                }
            } else {
                gameManager.addToGameSummary(String.format("Player %s played (%d %d).", action.player.getNicknameToken(), action.row, action.col));
            }

            lastAction = action;
            int result = hexGrid.MakeAction(action, player);
            if(result >= 4) { // Player wins
                gameManager.addToGameSummary(String.format("Player %s has made a line of 4.", action.player.getNicknameToken()));
                gameManager.addTooltip(action.player, player.getNicknameToken() + " made a line of 4");
                setWinner(player);
            } else if(result == 3) { // Player looses
                gameManager.addToGameSummary(String.format("Player %s has made a line of 3.", action.player.getNicknameToken()));
                gameManager.addTooltip(action.player, player.getNicknameToken() + " made a line of 3");
                setWinner(gameManager.getPlayer((turn+1) % gameManager.getPlayerCount()));
            } else {
                view.PaintSquare(graphicEntityModule, action);
            }

            validActions = hexGrid.getValidActions();

            if(validActions.size() == 0) {
                gameManager.addToGameSummary(GameManager.formatErrorMessage("No more moves, it's a tie!"));
                gameManager.addTooltip(gameManager.getPlayer(0), "tie");
                gameManager.addTooltip(gameManager.getPlayer(1), "tie");
                endGame();
            }
        } catch (NumberFormatException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " did not output a number!"));
            gameManager.addToGameSummary(GameManager.formatErrorMessage("Action was: " + player.GetActionOutput()));
            player.deactivate("Wrong output!");
            player.setScore(-1);
            endGame();
        } catch (TimeoutException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " did not output in time!"));
            player.deactivate(player.getNicknameToken() + " timeout!");
            player.setScore(-1);
            endGame();
        } catch (InvalidAction e) {
            gameManager.addToGameSummary(e.getMessage());
            gameManager.addToGameSummary(GameManager.formatErrorMessage("Action was: " + player.GetActionOutput()));
            player.deactivate(e.getMessage());
            player.setScore(-1);
            endGame();
        } catch (IndexOutOfBoundsException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " chose only one number!"));
            gameManager.addToGameSummary("Action was: " + player.GetActionOutput());
            player.deactivate(player.getNicknameToken() + " chose only one number!");
            player.setScore(-1);
            endGame();
        }
    }

    private void sendInputs(Player player, int turn) {
        if(turn <= 1) {
            player.sendInputLine(Integer.toString(player.getIndex() + 1));
        }
        player.sendInputLine("9");
        for(int i = 0; i < 9; ++i) {
            String text = "";
            for (int s : hexGrid.hexagons.get(i))
            {
                if(player.getIndex() == 0) {
                    text += s;
                } else {
                    if(s == 1) {
                        text += 2;
                    } else if(s == 2) {
                        text += 1;
                    } else {
                        text += 0;
                    }
                }
            }
            player.sendInputLine(text);
        }
        if (lastAction != null) {
            player.sendInputLine(lastAction.toString());
        } else {
            player.sendInputLine("-1 -1");
        }
    }

    private void setWinner(Player player) {
        gameManager.addToGameSummary(GameManager.formatSuccessMessage(player.getNicknameToken() + " won!"));
        player.setScore(1);
        endGame();
    }

    private Player getWinner() {
        if(gameManager.getPlayer(0).getScore() > gameManager.getPlayer(1).getScore()) {
            return gameManager.getPlayer(0);
        } else if(gameManager.getPlayer(0).getScore() < gameManager.getPlayer(1).getScore()) {
            return gameManager.getPlayer(1);
        } else {
            return null;
        }
    }

    private void endGame() {
        view.EndGameView(graphicEntityModule, lastAction, gameManager, getWinner(), hexGrid.CheckNeighbours(lastAction));
        gameManager.endGame();
    }

    @Override
    public void onEnd() {
        int[] scores = { gameManager.getPlayer(0).getScore(), gameManager.getPlayer(1).getScore() };
        String[] text = new String[2];
        if(scores[0] > scores[1]) {
            text[0] = "Won";
            text[1] = "Lost";
        } else if(scores[1] > scores[0]) {
            text[0] = "Lost";
            text[1] = "Won";
        } else {
            text[0] = "Draw";
            text[1] = "Draw";
        }
        endScreenModule.setScores(scores, text);
    }
}
