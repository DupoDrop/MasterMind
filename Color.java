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

    public String getName()
    {
        return name;
    }

    public byte getCode()
    {
        return code;
    }

    public static int getSize()
    {
        return size;
    }

    public static Color getAssociatedColor(String name) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.name == name)
                return col;
        }
        throw new ColorException();
    }

    public static Color getAssociatedColor(byte code) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.code == code)
                return col;
        }
        throw new ColorException();
    }
}
