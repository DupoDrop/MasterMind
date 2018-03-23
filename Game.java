public class Game
{
    private Color[] solution;
    private Color[][] combinations;
    private int[][] answer;
    private int nbAttempts;
    private int combinationSize;
    private int availableAttempts;

    public Game(int combinationSize, int availableAttempts)
    {
        this.combinationSize = combinationSize;
        this.availableAttempts = availableAttempts;
        solution = new Color[combinationSize];
        combinations = new Color[availableAttempts][combinationSize];
        answer = new int[availableAttempts][2];
        start_game();
    }

    public void start_game()
    {
        nbAttempts = 0;
        solution = Color.get_random_combination(this.combinationSize);
        System.out.print("New game started, the solution is: ");
        for(Color col: solution)
            System.out.print(" "+ col.get_name());
        System.out.println();
    }

    public Color[][] get_combinations()
    {
        Color[][] result = combinations.clone(); // clone in order to not allow the user to modify private field.
        return result;
    }

    public Color[] get_solution()
    {
        Color[] result = solution.clone(); // clone in order to not allow the user to modify private field.
        return result;
    }

    public int[][] get_answer()
    {
        int result[][] = answer.clone(); // clone in order to not allow the user to modify private field.
        return result;
    }

    public int getNbAttempts()
    {
        return nbAttempts;
    }

    public int[] analyse_combination(Color[] testedCombination) throws BadSizeException, NoMoreAttemptsException
    {
        if (testedCombination.length != combinationSize)
            throw new BadSizeException();

        if (nbAttempts >= availableAttempts)
            throw new NoMoreAttemptsException();

        combinations[nbAttempts] = testedCombination;
        answer[nbAttempts][0] = 0;
        answer[nbAttempts][1] = 0;

        for (int i = 0; i < combinationSize; i++)
        {
            /*
            I first check if it is at the right place for 2 reasons:
            First, as the user's goal is to place it correctly, it is likely that it will often be at the right place so it is a gain of time.
            Second, it avoid returning that it is good but wrong placed while there is also an other of the same color at the good place
             */

            if (testedCombination[i] == solution[i])
            {
                answer[nbAttempts][0]++;
                continue;
            }
            for (int j = 0; j < combinationSize; j++)
            {
                if (testedCombination[i] == solution[j])
                {
                    answer[nbAttempts][1]++;
                    break;
                }
            }
        }
        int[] result = answer[nbAttempts].clone(); // clone in order to not allow the user to modify private field.
        nbAttempts++;
        return result;
    }
}
