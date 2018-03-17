public enum Color
{
    RED("red", 0),
    BLUE("blue", 1),
    YELLOW("yellow", 2),
    GREEN("green", 3),
    WHITE("white", 4),
    BLACK("black", 5);

    private final String name;
    private final int code;

    private Color(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public int getCode()
    {
        return code;
    }

    public static int getAssociatedCode(String name) throws ColorException
    {
        for (Color col : Color.values())
        {
            if (col.name == name)
                return col.code;
        }
        throw new ColorException();
    }
}
