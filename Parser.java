import java.util.ArrayList;
import java.util.List;
public class Parser {
    private LexicalAnalyzer analyzer;
    private Token next;

    public Parser(LexicalAnalyzer analyzer) throws Exception {
        this.analyzer = analyzer;
        // initialize the first token for parser
        this.next = analyzer.getToken();
    }

    private void nextToken() throws Exception {
        next = analyzer.getToken();
    }

    //compares next to passed parameter
    //throws exception if it is not the correct token

    private void match(TokenType expectedType) throws Exception {
        if (next.getType() == expectedType) {
            nextToken();
        } else {
            //throws exception and reports where the error was discovered
            throw new Exception("Syntax Error: Expected " + expectedType + " but found " + next.getLexeme() + " at " + next.getRow() + ", " + next.getCol());
        }
    }

    public ParseTree parse() throws Exception {
        //new grammar starts with program
        ProgramNode root = parseProgram();
        if (next.getType() != TokenType.EOS) {
            throw new Exception("Syntax Error: Extra tokens found after end of expression: '"
                    + next.getLexeme() + "'");
        }
        return new ParseTree(root);
    }
    //Program ::= <Stmt_List>
    private ProgramNode parseProgram() throws Exception{
        //create statement list
        List<StatementNode> statement_list = new ArrayList<>();
        while(next.getType() != TokenType.EOS){
            statement_list.add(parseStatement());
        }
        return new ProgramNode(statement_list);
    }

    public ArithmeticExpressionNode parseArithmeticExpression() throws Exception {
        // Expressions can be 1 or 2 terms
        TermNode term = parseTerm();

        ArithmeticExpressionNode left = new UnaryExpressionNode(term);
        return parseExpressionPrime(left);
    }

    private ArithmeticExpressionNode parseExpressionPrime(ArithmeticExpressionNode left) throws Exception {
        if (next.getType() == TokenType.ADDITION) {
            // "+" <Term> <Expression_Prime
            match(TokenType.ADDITION);
            TermNode right = parseTerm();
            ArithmeticExpressionNode newLeft = new BinaryExpressionNode(left, TokenType.ADDITION, right);
            return parseExpressionPrime(newLeft);
        } else if (next.getType() == TokenType.SUBTRACTION) {
            // "-" <Term> <Expression_Prime>
            match(TokenType.SUBTRACTION);
            TermNode right = parseTerm();
            ArithmeticExpressionNode newLeft = new BinaryExpressionNode(left, TokenType.SUBTRACTION, right);
            return parseExpressionPrime(newLeft);
        }
        // null case: do nothing, just return left node
        return left;
    }

    private TermNode parseTerm() throws Exception {
        FactorNode factor = parseFactor();
        TermNode left = new UnaryTermNode(factor);
        return parseTermPrime(left);
    }

    private TermNode parseTermPrime(TermNode left) throws Exception {
        if (next.getType() == TokenType.MULTIPLICATION) {
            match(TokenType.MULTIPLICATION);
            FactorNode right = parseFactor();
            TermNode newLeft = new BinaryTermNode(left, TokenType.MULTIPLICATION, right);
            return parseTermPrime(newLeft);
        } else if (next.getType() == TokenType.DIVISION) {
            match(TokenType.DIVISION);
            FactorNode right = parseFactor();
            TermNode newLeft = new BinaryTermNode(left, TokenType.DIVISION, right);
            return parseTermPrime(newLeft);
        }
        // null case: do nothing
        return left;
    }

    private FactorNode parseFactor() throws Exception {
        if (next.getType() == TokenType.LEFT_PARENTHESES) {
            // "(" <Expression> ")"
            match(TokenType.LEFT_PARENTHESES);
            ArithmeticExpressionNode expr = parseArithmeticExpression();
            match(TokenType.RIGHT_PARENTHESES);
            return new ParenFactorNode(expr);
            // "-" <Expression>
        } else if (next.getType() == TokenType.SUBTRACTION) {
            match(TokenType.SUBTRACTION);
            ArithmeticExpressionNode expr = parseArithmeticExpression();
            return new NegFactor(expr);
        } else if (next.getType() == TokenType.INTEGER_LITERAL) {
            // <Number>
            int value = Integer.parseInt(next.getLexeme());
            match(TokenType.INTEGER_LITERAL);
            return new NumNode(value);

        } else if (next.getType() == TokenType.IDENTIFIER) {
            // <id>
            String id = next.getLexeme();
            match(TokenType.IDENTIFIER);
            return new IdNode(id);}
        else {
            throw new Exception("Syntax Error: Unexpected token " + next.getLexeme());
        }
    }

    // Helper to parse blocks of statements until an END or ELSE token is found
    private List<StatementNode> parseStatementList(TokenType exitToken1, TokenType exitToken2) throws Exception {
        List<StatementNode> stmts = new ArrayList<>();
        while (next.getType() != exitToken1 && next.getType() != exitToken2 && next.getType() != TokenType.EOS) {
            stmts.add(parseStatement());
        }
        return stmts;
    }

    //new method to parse logical expression
    private LogicalExpressionNode parseLogicalExpression() throws Exception {
        //<logical_expr> :: == <arithmetic_expression> relop <arithmetic_expression>
        ArithmeticExpressionNode left = parseArithmeticExpression();
        TokenType relop = next.getType();
//match relop variable to relevant token
        if (relop == TokenType.RELOP_LT || relop == TokenType.RELOP_LE ||
                relop == TokenType.RELOP_GT || relop == TokenType.RELOP_GE ||
                relop == TokenType.RELOP_EQ || relop == TokenType.RELOP_NE) {
            match(relop);
        }
        //throw error if there is no proper match
        else {
            throw new Exception("Syntax Error: Expected relational operator, found " + next.getLexeme());
        }

        ArithmeticExpressionNode right = parseArithmeticExpression();
        return new LogicalExpressionNode(left, relop, right);
    }

    private StatementNode parseStatement() throws Exception {
        if (next.getType() == TokenType.IDENTIFIER) {
            String id = next.getLexeme();
            match(TokenType.IDENTIFIER);
            match(TokenType.ASSIGNMENT);
            ArithmeticExpressionNode expression = parseArithmeticExpression();
            return new AssignmentNode(id, expression);
        } else if (next.getType() == TokenType.PRINT) {
            match(TokenType.PRINT);
            String id = next.getLexeme();
            match(TokenType.IDENTIFIER);
            return new PrintNode(id);
        } else if (next.getType() == TokenType.READ) {
            match(TokenType.READ);
            String id = next.getLexeme();
            match(TokenType.IDENTIFIER);
            return new ReadNode(id);
        } else if (next.getType() == TokenType.IF) {
            //<if_statement> -> “if” “(“ <logical_expression> ")" then”
            //<statement_list> “else” <statement_list> “end” “if”
            match(TokenType.IF);
            match(TokenType.LEFT_PARENTHESES);
            LogicalExpressionNode condition = parseLogicalExpression();
            match(TokenType.RIGHT_PARENTHESES);
            match(TokenType.THEN);

            List<StatementNode> thenBlock = parseStatementList(TokenType.ELSE, TokenType.END);
            List<StatementNode> elseBlock = null;

            if (next.getType() == TokenType.ELSE) {
                match(TokenType.ELSE);
                elseBlock = parseStatementList(TokenType.END, TokenType.END);
            }

            match(TokenType.END);
            match(TokenType.IF);
            return new IfNode(condition, thenBlock, elseBlock);
        } else if (next.getType() == TokenType.DO) {
            //<do_while_statement> -> “do” “while” “(“ <logical_expression> ")"
            //<statement_list> “end” “do”
            match(TokenType.DO);
            if (next.getType() == TokenType.WHILE) {
                match(TokenType.WHILE);
                match(TokenType.LEFT_PARENTHESES);
                LogicalExpressionNode condition = parseLogicalExpression();
                match(TokenType.RIGHT_PARENTHESES);

                List<StatementNode> loopBlock = parseStatementList(TokenType.END, TokenType.END);
                match(TokenType.END);
                match(TokenType.DO);
                return new DoWhileNode(condition, loopBlock);


            }
            //<do_statement> -> “do” <id> “=” <arithmetic_expression> “,”
            //<arithmetic_expression> <statement_list> “end” “do”
            else if (next.getType() == TokenType.IDENTIFIER) {
                String id = next.getLexeme();
                match(TokenType.IDENTIFIER);
                match(TokenType.ASSIGNMENT);
                ArithmeticExpressionNode start = parseArithmeticExpression();
                match(TokenType.COMMA);
                ArithmeticExpressionNode end = parseArithmeticExpression();

                List<StatementNode> loopBlock = parseStatementList(TokenType.END, TokenType.END);
                match(TokenType.END);
                match(TokenType.DO);
                return new DoNode(id, start, end, loopBlock);
            }
        }

        throw new Exception("Syntax Error: Expected statement, but found: '" + next.getLexeme() + "'");
    }
}