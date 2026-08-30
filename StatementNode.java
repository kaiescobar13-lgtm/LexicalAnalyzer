import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.Scanner;

//3 types of statements, Assignment, print, read, will be children of statementNode
interface StatementNode {
    //execute to be called by ParseTree class later;
    public abstract void execute();
}
class ProgramNode {
    private List<StatementNode> statement_list;
    //ProgramNode contains statements
    public ProgramNode(List<StatementNode> statement_list){
        this.statement_list = statement_list;
    }
    //execute all statements in statement list
    public void execute(){
        for(int i = 0; i < statement_list.size(); i++){
            statement_list.get(i).execute();
        }
    }
}

//Assignment ::= 'id = expression'
class AssignmentNode implements StatementNode {
    private String id;
    private ArithmeticExpressionNode expression;

    public AssignmentNode(String id, ArithmeticExpressionNode expression){
        this.expression = expression;
        this.id = id;
    }
    @Override
    public void execute(){
        memoryLocation.set(id, expression.evaluate());
    }
}

class PrintNode implements StatementNode{
    private String id;
    public PrintNode(String id){
        this.id = id;
    }
    @Override
    public void execute() {
        System.out.println(memoryLocation.get(id));
    }
}

class ReadNode implements StatementNode {
    private String id;
    private static Scanner scan = new Scanner(System.in);

    public ReadNode(String id){
        this.id = id;
    }

    @Override
    public void execute() {
        if(scan.hasNextInt()){
            memoryLocation.set(id, scan.nextInt());
        }else{
           //since we want program to terminate immediately if value does not represent an integer
            // we print an error statement then terminate program.
            System.err.println("Read needs id to be an Integer");
            System.exit(1);
        }
    }
}

class IfNode implements StatementNode {
    private LogicalExpressionNode expression;
    private List<StatementNode> thenBlock;
    private List<StatementNode> elseBlock;
    //<if_statement> → “if” “(“ <logical_expression> ")" then”
    //<statement_list> “else” <statement_list> “end” “if”

    public IfNode(LogicalExpressionNode expression, List<StatementNode> thenBlock, List<StatementNode> elseBlock){
        this.expression = expression;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void execute(){
        //if the expression evaluates to true, do the statements
        if(expression.evaluate()) {
            for (StatementNode statement : thenBlock) statement.execute();
            //if it evaluates to false AND has an elseBlock, do the elseBlock
        } else if (elseBlock != null){
            for (StatementNode statement : elseBlock) statement.execute();

        }
        }

    }
    class DoWhileNode implements StatementNode{
    private LogicalExpressionNode expression;
    private List<StatementNode> loop;

    public DoWhileNode(LogicalExpressionNode expression, List<StatementNode> loop){
        this.expression = expression;
        this.loop = loop;

    }
    @Override
        public void execute(){
        //while expression evaluates to true, evaluate statement
        while(expression.evaluate()){
            for(StatementNode statement: loop) {statement.execute();}
        }
    }

    }
    class  DoNode implements StatementNode{
    private String id;
    private ArithmeticExpressionNode startExpression;
    private ArithmeticExpressionNode endExpression;
    private List<StatementNode> loop;

    public DoNode(String id, ArithmeticExpressionNode startExpression, ArithmeticExpressionNode endExpression, List<StatementNode> loop){
        this.id = id;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.loop = loop;
    }


        @Override
        public void execute() {
            int start = startExpression.evaluate();
            int end = endExpression.evaluate();
            memoryLocation.set(id, start);
            while(memoryLocation.get(id) <= end){
                for(StatementNode statement: loop) statement.execute();
                memoryLocation.set(id, memoryLocation.get(id) + 1);

            }
        }
    }

