package view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static Text[] messageTexts = new Text[10];

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
        graphics.createRectangle().setWidth(WIDTH).setHeight(HEIGHT).setFillColor(0xababab).setZIndex(-2);

        Group texts = graphics.createGroup();
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
                tempLines.get(h).add(graphics.createLine().setX(centerX + 56 + (int)(radius * 1.75 * c)).setX2(centerX + 56 + (int)(radius * 1.75 * c)).setY(centerY - 33).setY2(centerY + 32).setLineColor(0x000000).setZIndex(5).setLineWidth(5));
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
        DrawPlayers(graphics, players);
    }

    private void DrawPlayer(GraphicEntityModule graphics, Player player) {
        // Borders
        int centerX = 145;
        if(player.getIndex() == 1) {
            centerX = 1542;
        }

        createHex(graphics, 225, centerX + 105, 280, 0, 0).setZIndex(-1).setAlpha(0.5);
        createHex(graphics, 131, centerX + 105, 540, 0, 1).setZIndex(0).setFillColor(player.getColorToken());
        createHex(graphics, 225, centerX + 105, 800, 0, 0).setZIndex(-1).setAlpha(0.5);


        String name = player.getNicknameToken();
        if(name.length() > 13) {
            name = name.substring(0,13);
        }
        graphics.createText(name)
                .setX(centerX + 100)
                .setAnchorX(0.5)
                .setY(115)
                .setFontSize(43)
                .setFontWeight(Text.FontWeight.BOLD)
                .setFontFamily("Calibri")
                .setFillColor(player.getColorToken());

        graphics.createSprite()
                .setImage(player.getAvatarToken())
                .setX(centerX)
                .setY(180)
                .setBaseWidth(200)
                .setBaseHeight(200);

        for(int i = 0; i < 10; ++i) {
            messageTexts[i] = graphics.createText("").setZIndex(2).setFontFamily("Calibri").setFontSize(30).setY(680 + i * 30);
        }
        moves[player.getIndex()] = graphics.createText("").setY(487).setX(centerX + 32).setZIndex(2).setFontSize(100).setFontFamily("Calibri");
    }

    private void DrawPlayers(GraphicEntityModule graphics, List<Player> players) {
        DrawPlayer(graphics, players.get(0));
        DrawPlayer(graphics, players.get(1));
    }

    public void DrawMessage(GraphicEntityModule graphics, Player player) {

        if(player.GetMessage() != null) {
            String[] messages = player.GetMessage().split("/");
            int numberOfMessages = messages.length;
            for(int i = 0; i < numberOfMessages; ++i) {
                if(messages[i].length() > 22) {
                    messageTexts[i].setText(messages[i].substring(0, 22));
                } else {
                    messageTexts[i].setText(messages[i]);
                }
                if(player.getIndex() == 0) {
                    messageTexts[i].setX(125);
                } else {
                    messageTexts[i].setX(1522);
                }
            }
        }
    }

    public void PaintSquare(GraphicEntityModule graphics, Action action) {
        moves[action.player.getIndex()].setText(action.row+ "  " + action.col);
        hexagonsList.get(action.col).get(action.row).setFillColor(action.player.getColorToken());
    }

    public void EndGameView(GraphicEntityModule graphics, Action action, GameManager gameManager, Player winner, List<Action> neighbours) {
        if(action == null)
            return;
        PaintSquare(graphics ,action);
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
