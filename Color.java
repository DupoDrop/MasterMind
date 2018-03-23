/**
 * Enumeration representing the different possible colors or tha mastermind game.
 */

public enum Color
{
    RED("red", (byte) 0),
    BLUE("blue", (byte) 1),
    YELLOW("yellow", (byte) 2),
    GREEN("green", (byte) 3),
    WHITE("white", (byte) 4),
    BLACK("black", (byte) 5);

    private final String name;
    private final byte code;
    private static final int size = Color.values().length;

    /**
     * Color's constructor.
     *
     * @param name a String representing the name of the Color
     * @param code a byte representing the code associated to the Color in the mastermind's protocol
     */
    Color(String name, byte code)
    {
        this.name = name;
        this.code = code;
    }

    public String get_name()
    {
        return name;
    }

    public byte get_code()
    {
        return code;
    }

    /**
     * Get the Color object which has the name given in argument
     *
     * @param name a String representing the name of the searched Color
     * @return the Color object which has as name the name specified as argument
     * @throws ColorException when the name given in argument does not correspond to a Color
     */
    public static Color get_associated_color(String name) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.name.equals(name))
                return col;
        }
        throw new ColorException();
    }

    /**
     * Get the Color object which has the code given in argument
     *
     * @param code a byte representing the code of the searched Color
     * @return the Color object which has as code the code specified as argument
     * @throws ColorException when the code given in argument does not correspond to a Color
     */
    public static Color get_associated_color(byte code) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.code == code)
                return col;
        }
        throw new ColorException();
    }

    /**
     * generate a random Color combination
     *
     * @param nbColor the number of Color that the combination should contain
     * @return an array of Color selected randomly
     */
    public static Color[] get_random_combination(int nbColor)
    {
        Color result[] = new Color[nbColor];
        try
        {
            for (byte i = 0; i < nbColor; i++)
                result[i] = (Color.get_associated_color((byte) (Math.floor(Math.random() * size))));
        }
        catch (ColorException e)
        {
            // A ColorException is not possible.
        }
        return result;
    }

}
