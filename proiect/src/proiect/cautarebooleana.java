package proiect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;



public class cautarebooleana {
	public enum LogicOp {AND, OR ,NOT};
	public static LogicOp getF(String val) {

		switch (val) {
		case "OR":
			return LogicOp.OR; 
		case "AND":
			return LogicOp.AND;
		case "NOT":
			return LogicOp.NOT;
		default:
			return null;
		}
	}

	public static HashMap<String, Integer> ANDfunction(HashMap<String, Integer> multime1, HashMap<String,Integer> multime2)
	{
		HashMap<String, Integer> fisi=new HashMap<String, Integer>();
		for (HashMap.Entry<String, Integer> f : multime1.entrySet())
		{
			if(multime2.containsKey(f.getKey()))
				fisi.put(f.getKey(), f.getValue());
		}
		for (HashMap.Entry<String, Integer> g : multime2.entrySet())
		{
			if(multime1.containsKey(g.getKey()))
				fisi.put(g.getKey(), g.getValue());
		}
		return fisi;
	}

	public static HashMap<String, Integer> ORfunction(HashMap<String, Integer> multime1, HashMap<String,Integer> multime2)
	{
		HashMap<String, Integer> fisi=new HashMap<String, Integer>();
		HashMap<String, Integer> min = new HashMap<String, Integer>();
		HashMap<String, Integer> max = new HashMap<String, Integer>();
		if(multime1.size()<multime2.size())
		{
			min=multime1;
			max=multime2;
		}
		else
		{
			min=multime2;
			max=multime1;
		}

		for (HashMap.Entry<String, Integer> f : min.entrySet())
		{
			if(!max.containsKey(f.getKey()))
				max.put(f.getKey(), f.getValue());
		}
		fisi=max;
		return fisi;
	}

	public static HashMap<String, Integer> NOTfunction(HashMap<String, Integer> multime1, HashMap<String,Integer> multime2)
	{

		HashMap<String, Integer> fisi=new HashMap<String, Integer>();
		for (HashMap.Entry<String, Integer> f : multime1.entrySet())
		{
			if(!multime2.containsKey(f.getKey()))
				fisi.put(f.getKey(), f.getValue());
		}
		return fisi;
	}
	
	public static HashMap<String, Integer> Search(String text,  HashMap<String, HashMap<String, Integer>> indexIndirect) {
	
		String[] cuv=text.split(" ");
		
		
		System.out.println("Forma canonica a cuvintelor este: ");
		cuv[0]=main.toCanonicalForm(cuv[0]);
		cuv[2]=main.toCanonicalForm(cuv[2]);
		System.out.println(cuv[0] + ", " + cuv[2]);
		System.out.println("");

		LogicOp operation = null;

		if(cuv[1].equals("OR") ||cuv[1].equals("AND") || cuv[1].equals("NOT") )
		{
			operation=getF(cuv[1]);
			if(operation==LogicOp.OR)
			{
				//Reuniune
				HashMap<String, Integer> OR_fisi= new HashMap();
				OR_fisi= ORfunction(indexIndirect.get(cuv[0]),indexIndirect.get(cuv[2]));
				Set<String> keys = OR_fisi.keySet();
				System.out.println("Rezultat:");
				
				for(String key: keys){
					System.out.println(key);
			
				}
				return OR_fisi;
			}

			else if(operation==LogicOp.AND)
			{
				// Intersectie
				HashMap<String, Integer> AND_fisi= new HashMap();
				AND_fisi= ANDfunction(indexIndirect.get(cuv[0]),indexIndirect.get(cuv[2]));
				Set<String> keys = AND_fisi.keySet();
				System.out.println("Rezultat:");
				for(String key: keys){
					System.out.println(key);
				
				}
				return AND_fisi;
				
			} 
			else if(operation==LogicOp.NOT)
			{
				// Substraction
				HashMap<String, Integer> NOT_fisi= new HashMap();
				NOT_fisi= NOTfunction(indexIndirect.get(cuv[0]),indexIndirect.get(cuv[2]));
				Set<String> keys = NOT_fisi.keySet();
				System.out.println("Rezultat:");
				
				for(String key: keys){
					System.out.println(key);
				
				}
				return NOT_fisi;
			} 
		}
		else
		{
			System.out.println("Nu s-a scris nicio operatie!");
			return null;
		}
		return null;
	}
}
