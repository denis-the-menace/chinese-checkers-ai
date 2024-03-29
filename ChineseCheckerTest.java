import java.util.*;

public class ChineseCheckerTest {
    static Random rand = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws CloneNotSupportedException {

        final int maximumExpansionCount = 100000;

        AdversarialSearch greedySearch = new AdversarialGreedySearch();

        BoardGameAgent agent1 = new ChineseCheckerCleverAgent(maximumExpansionCount);
        BoardGameAgent agent2 = new ChineseCheckerIdiotAgent(maximumExpansionCount);


        if (agent1 instanceof ChineseCheckerIdiotAgent && agent2 instanceof ChineseCheckerIdiotAgent) {
            System.err.println("Watching two idiots playing a game is silly!");
            return;
        }

        int boardSize = 8;
        ChineseCheckerState initialState = new ChineseCheckerState(boardSize);

        System.out.println("Game Board");
        System.out.println("----------");
        System.out.println(initialState);
        System.out.println();

        int agent1WinCount = 0;
        int agent2WinCount = 0;
        int gamePlayCount = 0;

        System.out.println("Player 1 :   P1 = " + agent1);
        System.out.println("Player 2 :   P2 = " + agent2);
        System.out.println();

        System.out.println("Playing with shuffled boards:");
        int tryCount = 0;
        int playCount = 100;
        while (gamePlayCount < playCount) {
            System.out.print(".");
            if ((++tryCount) % 25 == 0) {
                if (gamePlayCount > 0) {
                    System.out.print(",  [P1 " + getPercentage(agent1WinCount, gamePlayCount) + "%]  versus");
                    System.out.print("  [P2 " + getPercentage(agent2WinCount, gamePlayCount) + "%]");
                }
                System.out.println();
            }

            int maximumRandomMoveCount = 10;
            int randomMoveCount = rand.nextInt(maximumRandomMoveCount + 1);
            BoardState shuffledBoard = makeRandomMove(initialState, randomMoveCount);

            if (shuffledBoard != null) {
                BoardState switchedGameBoard = shuffledBoard.getSwitchedBoard();

                BoardState endGame = playGame(shuffledBoard, greedySearch, agent1, agent2);
                if (endGame != null) {
                    if (endGame.wins(Player.One)) {
                        //System.out.print(shuffledBoard);
                        //System.out.println("-- player 1 wins");
                        //System.out.println(endGame);
                        agent1WinCount++;
                        gamePlayCount++;
                    }
                    if (endGame.wins(Player.Two)) {
                        //System.out.print(shuffledBoard);
                        //System.out.println("-- player 2 wins");
                        //System.out.println(endGame);
                        agent2WinCount++;
                        gamePlayCount++;
                    }
                }

                endGame = playGame(switchedGameBoard, greedySearch, agent1, agent2);
                if (endGame != null) {
                    if (endGame.wins(Player.One)) {
                        //System.out.print(switchedGameBoard);
                        //System.out.println("__ player 1 wins __ (switched)");
                        //System.out.println(endGame);
                        agent1WinCount++;
                        gamePlayCount++;
                    }
                    if (endGame.wins(Player.Two)) {
                        //System.out.print(switchedGameBoard);
                        //System.out.println("__ player 2 wins __ (switched)");
                        //System.out.println(endGame);
                        agent2WinCount++;
                        gamePlayCount++;
                    }
                }
            }
        }

        if (gamePlayCount > 0) {
            System.out.println("\n");
            System.out.println(agent1 + " (player 1) wins " + getPercentage(agent1WinCount, gamePlayCount) + "%");
            System.out.println(agent2 + " (player 2) wins " + getPercentage(agent2WinCount, gamePlayCount) + "%");
        }
    }

    static double getPercentage(int count, int totalCount) {
        double percentage = (count / (double) totalCount * 100.0);

        percentage = (int) (percentage * 100) / 100.0;

        return percentage;
    }

    static BoardState playGame(BoardState gameBoard, AdversarialSearch adversarialSearch, BoardGameAgent agent1, BoardGameAgent agent2) {
        BoardState currentBoard = gameBoard;

        int maximumTurnCount = 1000;
        for (int i = 0; i < maximumTurnCount; i++) {
            // player 1
            currentBoard = adversarialSearch.getNextMove(currentBoard, agent1, Player.One).nextMove;
            if (currentBoard == null || currentBoard.isTerminal()) {
                return currentBoard;
            }

            // player 2
            currentBoard = adversarialSearch.getNextMove(currentBoard, agent2, Player.Two).nextMove;
            if (currentBoard == null || currentBoard.isTerminal()) {
                return currentBoard;
            }
        }

        return null;
    }

    static BoardState makeRandomMove(BoardState boardState, int randomMoveCount) throws CloneNotSupportedException {
        BoardState shuffledBoard = boardState.clone();

        for (int i = 0; i < randomMoveCount; i++) {
            for (Player player : Player.values()) {
                List<BoardState> successors = shuffledBoard.getSuccessors(player);

                if (successors.isEmpty()) {
                    return null;
                } else {
                    int moveIndex = rand.nextInt(successors.size());
                    shuffledBoard = successors.get(moveIndex);
                }
            }
        }

        return shuffledBoard;
    }

}
