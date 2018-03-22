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

    public static Color get_associated_color(String name) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.name.equals(name))
                return col;
        }
        throw new ColorException();
    }

    public static Color get_associated_color(byte code) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.code == code)
                return col;
        }
        throw new ColorException();
    }

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
