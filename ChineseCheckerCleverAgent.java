public class ChineseCheckerSancarAgent implements BoardGameAgent {

    final int maximumExpansionCount;

    ChineseCheckerSancarAgent(int maximumExpansionCount)
    {
        this.maximumExpansionCount = maximumExpansionCount;
    }
    @Override
    public int estimateDepth(BoardState boardState, Player player) {return 1;}

    @Override
    public long estimateExpansionCount(BoardState boardState, int m, Player player) {return 1;}

    @Override
    public double getUtility(BoardState boardState, Player player)
    {
        ChineseCheckerState chineseCheckerState = (ChineseCheckerState)boardState;

        // utility 1
        Point2D playerCog = chineseCheckerState.calculateCenterOfGravity(player);
        Point2D opponentRegionCog = (player == Player.One ? chineseCheckerState.player2cog : chineseCheckerState.player1cog);

        Point2D delta = Point2D.subtract(opponentRegionCog, playerCog);

        double utility1 = 1.0 / (1.0 + delta.manhattan());


        // utility 2
        Player opponent = player.getOpponent();

        int playerInplaceCount = 0;
        int opponentInplaceCount = 0;

        for (int y=0; y<chineseCheckerState.boardSize; y++) {
            for (int x=0; x<chineseCheckerState.boardSize; x++) {
                if (chineseCheckerState.initialBoardCells[y][x] == opponent && chineseCheckerState.boardCells[y][x] == player) {
                    playerInplaceCount++;
                }

                if (chineseCheckerState.initialBoardCells[y][x] == player && chineseCheckerState.boardCells[y][x] == opponent) {
                    opponentInplaceCount++;
                }
            }
        }

        double utility2 = (1.0 + playerInplaceCount) / (1.0 + opponentInplaceCount);

        //utility 3
        //Check the gap between player's and opponent's pieces. If there are more gaps in player's pieces
        // (standard deviation is higher) then the opponent can jump over the player's pieces and get to
        // their spot to win the game and vice versa.
        Point2D playerSD = chineseCheckerState.calculateStandardDeviation(player);
        //Point2D opponentSD = (player == Player.One ? chineseCheckerState.player2SD : chineseCheckerState.player1SD);

        double utility3 = 1.0;
        utility3 = 1.0/(1.0 + playerSD.manhattan());
        //playerSD arttikca utility3 azalicak yani combinedUtility dusecek
        //utility3 = 1.0/(1.0 + opponentSD.manhattan());
//        if(playerSD.manhattan()>opponentSD.manhattan())
//            utility3 = 1.0/(1.0 + playerSD.manhattan());
//        else if(playerSD.manhattan()<opponentSD.manhattan())
//            utility3 = 1.0/(1.0 + opponentSD.manhattan());

        // combined utility
        return (1.0 + utility1) * (1.0 + utility2) * (1.0 + utility3);
    }

    @Override
    public String toString() {
        return "Einstein Agent   75 < IQ < 100";
    }

}