package proiect;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class index {
	public static void main(String[] args) throws IOException {
        main l3 = new main("./director/");
        HashMap<String, HashMap<String, Integer>> directIndex = l3.directIndex();
        HashMap<String, HashMap<String, Integer>> indirectIndex = l3.indirectIndex();
    
        
        
		//cautarebooleana.Search("queen OR public AND prototyp", indirectIndex);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("");
        System.out.println("Cautare booleana");
        System.out.println("");
        String text= "indexes AND public";
        System.out.println(text);
        System.out.println("");
		cautarebooleana.Search(text, indirectIndex);
    }
}
