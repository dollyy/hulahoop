import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Test {

    public static void main(String[] args){
        Map<Integer,Integer> hashMap=new HashMap<>();
        hashMap.put(1,11);
        hashMap.put(2,22);
        hashMap.put(3,33);
        hashMap.put(4,44);
        for(Map.Entry<Integer, Integer> entry: hashMap.entrySet()){
            System.out.println(entry.getKey()+","+entry.getValue());
        }
        System.out.println("--------------------------------------");
        Map<Integer,Integer> hasTable=new Hashtable<>();
        hasTable.put(1,11);
        hasTable.put(2,22);
        hasTable.put(3,33);
        hasTable.put(4,44);
        for(Map.Entry<Integer,Integer> entry : hasTable.entrySet()){
            System.out.println(entry.getKey()+","+entry.getValue());
        }
    }
}
