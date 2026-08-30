abstract class FactorNode extends TermNode {
}

 class NumNode extends FactorNode {
    private int value;

    public NumNode(int value) {
        this.value = value;
    }

    @Override
    public int evaluate() {
        return value;
    }
}

class ParenFactorNode extends FactorNode {
    private ArithmeticExpressionNode expr;

    public ParenFactorNode(ArithmeticExpressionNode expr) {
        this.expr = expr;
    }

    @Override
    public int evaluate() {
        return expr.evaluate();
    }
}

class NegFactor extends FactorNode {
    private ArithmeticExpressionNode expr;

    public NegFactor(ArithmeticExpressionNode expr) {
        if(expr == null){
            throw new IllegalArgumentException("NegFactor requires a valid ExpressionNode, received null.");
        }
        this.expr = expr;
    }

    @Override
    public int evaluate() {
        return -expr.evaluate();
    }
}