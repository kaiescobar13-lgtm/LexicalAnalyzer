// Term inherits from expression

abstract class TermNode extends ArithmeticExpressionNode {
}

class BinaryTermNode extends TermNode {
    private TermNode left;
    private TokenType operator;
    private FactorNode right;

    public BinaryTermNode(TermNode left, TokenType operator, FactorNode right) {
        //Children shouldn't be null
        if (left == null || right == null) {
            throw new IllegalArgumentException("Child nodes in BinaryTermNode cannot be null.");
        }
        // should be multiplication or division.
        if (operator != TokenType.MULTIPLICATION && operator != TokenType.DIVISION) {
            throw new IllegalArgumentException("BinaryTermNode only accepts MULTIPLICATION or DIVISION operators. Received: " + operator);
        }
        this.left = left;
        this.operator = operator;
        this.right = right;

    }

    @Override
    public int evaluate() {
        if (operator == TokenType.MULTIPLICATION) {
            return left.evaluate() * right.evaluate();
        } else {
            int rightValue = right.evaluate();
            if(rightValue == 0){
                throw new ArithmeticException("Evaluation Error: Can't divide by 0");
            }
            return left.evaluate() / rightValue;
        }
    }
}

class UnaryTermNode extends TermNode {
    private FactorNode factor;

    public UnaryTermNode(FactorNode factor) {

        this.factor = factor;

    }

    @Override
    public int evaluate() {
        return factor.evaluate();
    }
}

