//enumerated tokenTypes
enum TokenType{
    //added new tokens if, then, else, end, do, while, comma, relop_lt, relop_le, relop_gt, relop_ge, relop_eq, relop_ne
    LEFT_PARENTHESES, RIGHT_PARENTHESES, MULTIPLICATION, DIVISION, ADDITION, SUBTRACTION,  INTEGER_LITERAL, EOS,
    IDENTIFIER, PRINT, READ, ASSIGNMENT, IF, THEN, ELSE, END, DO, WHILE, COMMA,
    RELOP_LT, RELOP_LE, RELOP_GT, RELOP_GE, RELOP_EQ, RELOP_NE
}
public class Token {

    //4 component fields
    private TokenType type;
    private String lexeme;
    private int row;
    private int col;
    //constructor
    public Token(TokenType type, String lexeme, int row, int col){
        try{
        this.type = type;
        this.lexeme = lexeme;
        this.row = row;
        this.col = col;
            // check for invalid row values
            if (row < 1) {
                throw new IllegalArgumentException("Invalid row argument: " + row + ". Row must be 1 or greater.");
            }
            // check for invalid column values
            if (col < 0) {
                throw new IllegalArgumentException("Invalid column argument: " + col + ". Column must be 0 or greater.");
            }
        }
        catch(IllegalArgumentException e){
            System.err.println("Illegal Token Argument");
        }
    }
    //4 get methods
    public TokenType getType(){
        return type;
    }
    public String getLexeme(){
        return lexeme;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    //toString method for ease of debugging
    @Override
    public String toString(){
        return String.format("Token(" + type + ", " + lexeme + ", " + row + ", " + col + ")");
    }
}
