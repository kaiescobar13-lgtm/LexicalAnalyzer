import java.util.HashMap;
public class memoryLocation {

    //"easiest way to implement memory is through a map"
    //key is string and object stored is Integer
    private static HashMap<String, Integer> memory = new HashMap<>();
    public static int get(String id){
        //defaults to 0 if there is no value currently there
        return memory.getOrDefault(id, 0);

    }

    public static void set(String id, int value){
        memory.put(id, value);
    }

}
