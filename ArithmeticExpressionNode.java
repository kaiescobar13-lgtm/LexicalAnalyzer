abstract class ArithmeticExpressionNode {
    public abstract int evaluate();
}

class BinaryExpressionNode extends ArithmeticExpressionNode {
    private ArithmeticExpressionNode left;
    private TokenType operator;
    private TermNode right;

    public BinaryExpressionNode(ArithmeticExpressionNode left, TokenType operator, TermNode right) {
        //Children shouldn't be null
        if (left == null || right == null) {
            throw new IllegalArgumentException("Child nodes in BinaryExpressionNode cannot be null.");
        }
        // should be addition or subtraction.
        if (operator != TokenType.ADDITION && operator != TokenType.SUBTRACTION) {
            throw new IllegalArgumentException("BinaryExpressionNode only accepts ADDITION or SUBTRACTION operators. Received: " + operator);
        }
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public int evaluate() {
        if (operator == TokenType.ADDITION) {
            return left.evaluate() + right.evaluate();
        } else {
            return left.evaluate() - right.evaluate();
        }
    }
}

class UnaryExpressionNode extends ArithmeticExpressionNode {
    private TermNode term;

    public UnaryExpressionNode(TermNode term) {
        this.term = term;
    }

    @Override
    public int evaluate() {
        return term.evaluate();
    }
}


