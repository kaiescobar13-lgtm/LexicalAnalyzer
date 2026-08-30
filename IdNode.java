//id extends factor
public class IdNode extends FactorNode{
    private String id;
    //id node stores id
    public IdNode(String id){
        this.id = id;
    }

    @Override
    public int evaluate() {
        return memoryLocation.get(id);
    }
}
