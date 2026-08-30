//<logical_expr> :: == <arithmetic_expression> relop <arithmetic_expression>
public class LogicalExpressionNode {
    private ArithmeticExpressionNode left;
    private TokenType relop;
    private ArithmeticExpressionNode right;

    public LogicalExpressionNode(ArithmeticExpressionNode left, TokenType relop, ArithmeticExpressionNode right){
        this.left = left;
        this.right = right;
        this.relop = relop;
    }
    public boolean evaluate(){
        int leftValue = left.evaluate();
        int rightValue = right.evaluate();
        //evaluate expression based on what relational operator is
        switch(relop){
            case RELOP_LT: return leftValue < rightValue;
            case RELOP_LE: return leftValue <= rightValue;
            case RELOP_GT: return leftValue > rightValue;
            case RELOP_GE: return leftValue >= rightValue;
            case RELOP_EQ: return leftValue == rightValue;
            case RELOP_NE: return leftValue != rightValue;
            default: return false;



        }

    }

}
