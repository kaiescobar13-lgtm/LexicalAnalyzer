public class LexicalAnalyzer {
    private String sourceCode;
    //keep track of current character for source code
    private int sourceIndex;
    int row;
    int col;
    public LexicalAnalyzer(String sourceCode){
        this.sourceCode = sourceCode;
        this.sourceIndex = 0;
        this.row = 1;
        this.col = 1;
    }
    public Token getToken() throws Exception{
        //handles whitespace
        while(sourceIndex < sourceCode.length() && Character.isWhitespace(sourceCode.charAt(sourceIndex))){
            //track row and column now
            //if new line character, increment row reset column
            if(sourceCode.charAt(sourceIndex) == '\n'){
                row++;
                col = 1;

            } //if not new line, increment column
            else{
                col++;
            }
            sourceIndex++;
        }
        if(sourceIndex == sourceCode.length()){
            return new Token(TokenType.EOS, "EOS", row, col);
        }
        char currentChar = sourceCode.charAt(sourceIndex);
        sourceIndex++;
        col++;
        switch (currentChar) {
            case '(':
                return new Token(TokenType.LEFT_PARENTHESES, "(", row, col);
            case ')':
                return new Token(TokenType.RIGHT_PARENTHESES, ")", row, col);
            case '*':
                return new Token(TokenType.MULTIPLICATION, "*", row, col);
            case '/':
                return new Token(TokenType.DIVISION, "/", row, col);
            case '+':
                return new Token(TokenType.ADDITION, "+", row, col);
            case '-':
                return new Token(TokenType.SUBTRACTION, "-", row, col);
            case '=':
                return new Token(TokenType.ASSIGNMENT, "=", row, col);
            case ',':
                return new Token(TokenType.COMMA, ",", row, col);
            case '.':
                StringBuilder relop = new StringBuilder();
                relop.append(currentChar);
                while (sourceIndex < sourceCode.length() && sourceCode.charAt(sourceIndex) != '.'){
                    relop.append(sourceCode.charAt(sourceIndex));
                sourceIndex++;
                col++;
        }
                if(sourceIndex<sourceCode.length() && sourceCode.charAt(sourceIndex) == '.'){
                    relop.append('.');
                    sourceIndex++;
                    col++;
                }
                return newRelop(relop.toString().toLowerCase(), row, col);
            //if not a symbol then the character should be an integer, we need to validate that it is an integer
            //and handle the case that it is, or handle the exception if it is not a valid integer
            default:
                //we want the integer to be represented as one long integer, rather than separate integers.
                //for example we would want 142 to be represented as 142 rather than 1, 4, 2.
                //We construct a string containing the numbers that follow one another to make one large number.
                if(Character.isDigit(currentChar)){
                StringBuilder num = new StringBuilder();
                num.append(currentChar);
                while(sourceIndex < sourceCode.length() && Character.isDigit(sourceCode.charAt(sourceIndex))){
                    num.append(sourceCode.charAt(sourceIndex));
                    sourceIndex++;
                    col++;
                }
                return new Token(TokenType.INTEGER_LITERAL, num.toString(), row, col);
                }
                //if the character is a letter we need to start building the identifier/ key Twords
                if(Character.isLetter(currentChar)){
                    StringBuilder identifier = new StringBuilder();
                    //add currentChar to the string
                    identifier.append(currentChar);
                    //check if next char is letter or digit because identifiers are
                    //<rest_id> -> letter <rest_id> | digit <rest_id> | letter | digit
                    while(sourceIndex < sourceCode.length() && (Character.isLetter(sourceCode.charAt(sourceIndex)) || Character.isDigit(sourceCode.charAt(sourceIndex)))){
                        identifier.append(sourceCode.charAt(sourceIndex));
                        sourceIndex++;
                        col++;
                    }
                    //identifiers are NOT case sensitive
                    String lowerIdentifier = identifier.toString().toLowerCase();
                    //if we have any keywords we match to that token
                    return matchIdentifier(lowerIdentifier, row, col);
                }
                //throw characters that don't fit language
                throw new Exception("Unexpected character: " + currentChar + "\nLocated at position: " + row + ", " + (col-1) );
        }

    }

    //gets proper relational operator
    Token newRelop(String op, int row, int col) throws Exception {
        switch (op){
            case ".lt.": return new Token(TokenType.RELOP_LT, op, row, col);
            case ".le.": return new Token(TokenType.RELOP_LE, op, row, col);
            case ".gt.": return new Token(TokenType.RELOP_GT, op, row, col);
            case ".ge.": return new Token(TokenType.RELOP_GE, op, row, col);
            case ".eq.": return new Token(TokenType.RELOP_EQ, op, row, col);
            case ".ne.": return new Token(TokenType.RELOP_NE, op, row, col);
            default: throw new Exception("Invalid relational operator: " + op);



        }

    }
    //matches identifier to key word
    Token matchIdentifier(String lowerIdentifier, int row, int col){
        if(lowerIdentifier.equals("print")){
            return new Token(TokenType.PRINT, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("read")){
            return new Token(TokenType.READ, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("if")){
            return new Token(TokenType.IF, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("then")){
            return new Token(TokenType.THEN, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("else")){
            return new Token(TokenType.ELSE, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("end")){
            return new Token(TokenType.END, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("do")){
            return new Token(TokenType.DO, lowerIdentifier, row, col);
        }
        else if(lowerIdentifier.equals("while")){
            return new Token(TokenType.WHILE, lowerIdentifier, row, col);
        }
        //otherwise we have an identifier and match that token
        else{
            return new Token(TokenType.IDENTIFIER, lowerIdentifier, row, col);
        }
    }

}
