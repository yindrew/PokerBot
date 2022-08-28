import java.util.Random;

/**
 * A representation of the game Colonel Blotto with two players
 * finding the optimal strategy
 */
public class ColonelBlotto {
    public static final int NUM_ACTIONS = 21;
    public static final Random random = new Random();
 
    // player one's strategies and regrets 
    double[] regretSum = new double[NUM_ACTIONS],
            strategySum = new double[NUM_ACTIONS],
            strategy = new double[NUM_ACTIONS];

    // player two's strategies and regrets
    double[] oppRegretSum = new double[NUM_ACTIONS],
            oppStrategySum = new double[NUM_ACTIONS],
            oppStrategy = new double[NUM_ACTIONS];
    
    /**
     * total comprehensive strategy list. each digit represents how many soldiers 
     * are put into each battlefield
     */
    String[] strategyList = { "500", "410", "401", "320", "311", "302",
            "230", "221", "212", "203", "140", "131", "122", "113", "104",
            "050", "041", "032", "023", "014", "005" };
    
    /**
     * get the strategy for player one
     * @return the strategy for player one
     */
    public double[] getStrategy() {
        double normalizingSum = 0;
        for(int a = 0; a < NUM_ACTIONS; a++){
            strategy[a] = regretSum[a] > 0 ? regretSum[a] : 0;
            normalizingSum += strategy[a];
        }

        for(int x = 0; x < NUM_ACTIONS; x++){
            if(normalizingSum > 0){
                strategy[x] /= normalizingSum;
            }
            else{
                strategy[x] = 1.0 / NUM_ACTIONS;
            }
            strategySum[x] += strategy[x];
        }
        
        return strategy;
    }

    /**
     * get the strategy for player 2
     * @return the strategy for player 2
     */
    public double[] getOppStrategy() {
        double normalizingSum = 0;
        for(int a = 0; a < NUM_ACTIONS; a++){
            oppStrategy[a] = oppRegretSum[a] > 0 ? oppRegretSum[a] : 0;
            normalizingSum += oppStrategy[a];
        }

        for(int x = 0; x < NUM_ACTIONS; x++){
            if(normalizingSum > 0){
                oppStrategy[x] /= normalizingSum;
            }
            else{
                oppStrategy[x] = 1.0 / NUM_ACTIONS;
            }
            oppStrategySum[x] += oppStrategy[x];
        }
        
        return oppStrategy;
    }

    /**
     * get a random action based on frequencies
     * @param strategy the strategy being used
     * @return a action based on a strategy
     */
    public String getAction(double[] strategy){
        double r = random.nextDouble();
        int a = 0;
        double cumulativeProbability = 0;
        while (a < NUM_ACTIONS - 1) {
            cumulativeProbability += strategy[a];
            if ( r < cumulativeProbability) {
                break;
            }
            a++;
        }
        return strategyList[a];
    }

    /**
     * comparing 2 different strategies
     * @param strat1 first strategy
     * @param strat2 second strategy
     * @return whoever won the battle
     */
    public int getDifference(String strat1, String strat2) {
        int sum = 0;
        for(int x = 0; x < strat1.length() ;x++){
            int player = strat1.charAt(x) - '0';
            int opp = strat2.charAt(x) - '0';
                if (player > opp){
                    sum++;
                }
                else if (player < opp) {
                    sum--;
                }

        }
        // sum represents the difference between the 2 strategies
        return sum;

    }

    /**
     * training the bots by playing a multitude of times
     * @param iterations the number of times the players play against each other
     */
    public void train(int iterations) {
        for (int i = 0; i < iterations; i++) {
            double[] playerStrat = getStrategy();
            String action = getAction(playerStrat);
            double[] oppStrat = getOppStrategy();
            String oppAction = getAction(oppStrat);

            int actionUtil = getDifference(action, oppAction);

            for(int x = 0; x < NUM_ACTIONS; x++){
                int potentialAction = getDifference(strategyList[x], oppAction);
                regretSum[x] += potentialAction - actionUtil;
            }

            for(int x = 0; x < NUM_ACTIONS; x++){
                int potentialAction = getDifference(strategyList[x], action);
                oppRegretSum[x] += potentialAction + actionUtil;
            }

        }
    }

    /**
     * finds the final average strategy after a cetain number of hands
     * @param strat the strategy used
     * @return the average strategy
     */
    public double[] getAverageStrategy(double[] strat) {
        double[] avgStrat = new double[NUM_ACTIONS];
        double normalizingSum = 0;
        for (int x = 0; x < NUM_ACTIONS; x++) {
            normalizingSum += strat[x];
        }
        for (int x = 0; x < NUM_ACTIONS; x++) {
            if (normalizingSum > 0) {
                avgStrat[x] = strat[x] / normalizingSum;
            } else {
                avgStrat[x] = 1.0 / NUM_ACTIONS;
            }

        }

        return avgStrat;
    }

}