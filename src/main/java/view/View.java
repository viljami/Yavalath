package view;

import java.util.ArrayList;
import java.util.List;

import com.codingame.game.Action;
import com.codingame.game.Player;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import view.toggle.ToggleModule;

public class View {
    // Constants
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;


    private List<List<Polygon>> hexagonsList = new ArrayList<>();
    private static Text[] moves = new Text[2];
    // Needed to add this because some people had bug rendering hexagons.
    private List<List<Line>> tempLines = new ArrayList<>();
    private static Text[][] messagesTexts = new Text[2][6];
    private Group[] playersHud = new Group[2];

    private Polygon createHex(GraphicEntityModule graphics, double radius, int centerX, int centerY, int row, int d) {
        Polygon hex = graphics.createPolygon();
        // Creates HexGrid
        for (int i = 0; i < 6; i++) {
            int x = (int)(radius * 1.75 * row + centerX + radius * Math.cos((d + i * 2) * Math.PI / 6D));
            int y = (int)(centerY + radius * Math.sin((d + i * 2) * Math.PI / 6D));
            hex.addPoint(x, y);
        }
        hex.setFillColor(0xE7BA7B).setLineWidth(5).setLineColor(0x000000).setZIndex(0);

        return hex;
    };

    public View(GraphicEntityModule graphics, TooltipModule tooltips, ToggleModule toggle, List<Player> players) {
        Group texts = graphics.createGroup();

        // Background
        graphics.createRectangle().setWidth(WIDTH).setHeight(HEIGHT).setFillColor(0xababab).setZIndex(0);

        texts.setZIndex(2);

        double radius = 65;
        int centerX = 735;
        int centerY = 150;
        int counter = 5;

        // Generates hex grid
        for(int h = 0; h < 9; ++h) {
            hexagonsList.add(h, new ArrayList<>());
            tempLines.add(h, new ArrayList<>());
            for(int c = 0; c < counter; ++c) {
                hexagonsList.get(h).add(c, createHex(graphics, radius, centerX, centerY, c, 1));
                tooltips.setTooltipText(hexagonsList.get(h).get(c), c + " " + h);
                texts.add(graphics.createText(c + "  " + h).setX(centerX + (int)(radius * 1.75 * c)).setY(centerY).setAnchorX(0.5).setAnchorY(0.5).setFontSize(40));
                tempLines.get(h).add(graphics.createLine().setX(centerX + 56 + (int)(radius * 1.75 * c)).setX2(centerX + 56 + (int)(radius * 1.75 * c)).setY(centerY - 33).setY2(centerY + 32).setLineColor(0x000000).setZIndex(1).setLineWidth(5));
            }
            if(h < 4) {
                centerX -= radius * 0.875;
                centerY += radius * 1.5;
                counter++;
            } else {
                centerX += radius * 0.875;
                centerY += radius * 1.5;
                counter--;
            }
        }
        toggle.displayOnToggleState(texts, "debugToggle", true);

        for (Player player: players) {
            playersHud[player.getIndex()] = graphics.createGroup().setZIndex(1);
            DrawPlayer(graphics, player);
        }
    }

    private void DrawPlayer(GraphicEntityModule graphics, Player player) {
        int centerX = 145;
        if(player.getIndex() == 1) {
            centerX = 1565;
        }

        // Borders
        playersHud[player.getIndex()].add(createHex(graphics, 225, centerX + 105, 280, 0, 0).setZIndex(0));
        playersHud[player.getIndex()].add(graphics.createLine().setX(centerX + 330).setX2(centerX + 217).setY(280).setY2(85).setLineColor(0x000000).setZIndex(1).setLineWidth(5));
        playersHud[player.getIndex()].add(createHex(graphics, 131, centerX + 105, 540, 0, 1).setZIndex(1).setFillColor(player.getColorToken()));
        playersHud[player.getIndex()].add(graphics.createLine().setX(centerX + 217).setX2(centerX + 217).setY(540 - 66).setY2(540 + 66).setLineColor(0x000000).setZIndex(1).setLineWidth(5));
        playersHud[player.getIndex()].add(createHex(graphics, 225, centerX + 105, 800, 0, 0).setZIndex(0));
        playersHud[player.getIndex()].add(graphics.createLine().setX(centerX + 330).setX2(centerX + 217).setY(800).setY2(605).setLineColor(0x000000).setZIndex(1).setLineWidth(5));


        String name = player.getNicknameToken();
        if(name.length() > 13) {
            name = name.substring(0,13);
        }
        playersHud[player.getIndex()].add(graphics.createText(name)
                .setX(centerX + 100)
                .setAnchorX(0.5)
                .setY(130)
                .setFontSize(40)
                .setFontWeight(Text.FontWeight.BOLD)
                .setFontFamily("Calibri")
                .setFillColor(player.getColorToken()));

        playersHud[player.getIndex()].add(graphics.createSprite()
                .setImage(player.getAvatarToken())
                .setX(centerX)
                .setY(180)
                .setBaseWidth(200)
                .setBaseHeight(200)
                .setMask(createHex(graphics, 90, centerX + 105, 280, 0 ,0).setFillColor(player.getColorToken())));


        for(int i = 0; i < messagesTexts[player.getIndex()].length; ++i) {
            playersHud[player.getIndex()].add(messagesTexts[player.getIndex()][i] = graphics.createText("").setZIndex(2).setFontFamily("Calibri").setX(centerX-20).setFontSize(40).setY(680 + i * 40));
        }

        playersHud[player.getIndex()].add(moves[player.getIndex()] = graphics.createText("").setY(487).setX(centerX + 105).setZIndex(2).setFontSize(100).setFontFamily("Calibri").setAnchorX(0.5));
    }

    public void DrawMessage(Player player) {
        if(player.GetMessage() != null) {
            String[] messages = player.GetMessage().split("/");
            int numberOfMessages = messages.length;
            for(int i = 0; i < numberOfMessages && i < messagesTexts[player.getIndex()].length; ++i) {
                if(messages[i].length() > 16) {
                    messagesTexts[player.getIndex()][i].setText(messages[i].substring(0, 16));
                } else {
                    messagesTexts[player.getIndex()][i].setText(messages[i]);
                }
            }
        }
    }

    public void PaintSquare(Action action) {
        moves[action.player.getIndex()].setText(action.row+ "  " + action.col);
        hexagonsList.get(action.col).get(action.row).setFillColor(action.player.getColorToken());
    }

    public void EndGameView(Action action, GameManager gameManager, Player winner, List<Action> neighbours) {
        if(action == null)
            return;
        PaintSquare(action);
        gameManager.setFrameDuration(2000);

        if(winner == null) { // Its a tie
            return;
        }

        // If lastPlayed is the winner
        if(action.player.getIndex() == winner.getIndex()) {
            for(int h = 0; h < 9; ++h) {
                for(int i = 0; i < hexagonsList.get(h).size(); ++i) {
                    boolean found = false;
                    for(Action a : neighbours) {
                        if(h == a.col && i == a.row) {
                            hexagonsList.get(h).get(i).setZIndex(1);
                            found = true;
                        }
                    }
                    if(!found) {
                        tempLines.get(h).get(i).setLineAlpha(0);
                        hexagonsList.get(h).get(i).setAlpha(0.35).setLineAlpha(0);
                    }
                }
            }
        } else {
            for(Action a : neighbours) {
                tempLines.get(a.col).get(a.row).setLineAlpha(0);
                hexagonsList.get(a.col).get(a.row).setAlpha(0.35).setLineAlpha(0);
            }
        }
    }
}
