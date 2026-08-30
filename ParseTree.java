public class ParseTree {
    //new root node ProgramNode
    private ProgramNode programNode;
    //Holds reference to root node
    public ParseTree(ProgramNode programNode) {
        this.programNode = programNode;

    }

    //executes root node
    public void execute(){
        programNode.execute();
    }

}