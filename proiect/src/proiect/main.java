package proiect;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class main {
	 	private String folder;

	    main(String websiteFolder)
	    {
	        this.folder = websiteFolder;
	       
	    }
	  //extragerea elementului <TITLE>
	    private String getTitle(Document doc)
	    {
	    	String title = doc.select("title").text();
			return title;
	    }
	  //extragerea cuvintelor cheie
		
		//atributul name are valorea "keywords"
	    private String getKeywords(Document doc)
	    {
	        Element keywords = doc.selectFirst("meta[name=keywords]");
	        String keywordsString = "";
	        if (keywords == null) {
	            //System.out.println("Tag-ul <meta name=\"keywords\"> nu exista!");
	        } else {
	            keywordsString = keywords.attr("content");
	            //System.out.println("Am luat cuvintele!");
	        }
	        return keywordsString;
	    }

	    //atributul name are valorea "description"
	    private String getDescription(Document doc)
	    {
	        Element description = doc.selectFirst("meta[name=description]");
	        String descriptionString = "";
	        if (description == null) {
	           // System.out.println("Tag-ul <meta name=\"description\">nu exista!");
	        } else {
	            descriptionString = description.attr("content");
	            //System.out.println("Am luat descrierea!");
	        }
	        return descriptionString;
	    }
	  //atributul name are valorea "robots"
	    private String getRobots(Document doc) 
	    {
	        Element robots = doc.selectFirst("meta[name=robots]");
	        String robotsString = "";
	        if (robots == null) {
	           // System.out.println("Tag-ul <meta name=\"robots\"> nu exista!");
	        } else {
	            robotsString = robots.attr("content");
	           // System.out.println("Am luat ista de robots!");
	        }
	        return robotsString;
	    }
	  //linkuri
	    private String getLinks(Document doc) 
	    {
	    	String link="";
			Element linkExist = doc.selectFirst("a[abs:href]");
			if(linkExist == null) {
				//System.out.println("Nu exista link absolut!");
			}
			else
			{
				link =  linkExist.attr("href");
			}
			return link;
	    }
	    // pun intr-un fisier continutul siteului
	    private File lab1(Document doc, File fishtml) throws IOException 
	    {
	        StringBuilder sb = new StringBuilder();
	        sb.append(getTitle(doc)); 
	        sb.append(System.lineSeparator());
	        sb.append(getKeywords(doc)); 
	        sb.append(System.lineSeparator());
	        sb.append(getDescription(doc));
	        sb.append(System.lineSeparator());
	        sb.append(doc.body().text());
	        String text = sb.toString();

	        // generez fisierul
	        StringBuilder fisier = new StringBuilder(fishtml.getAbsolutePath());
	        //fisier.append(".txt");
	        String fis = fisier.toString();

	        // scriu rezultatul
	        FileWriter out = new FileWriter(new File(fis), false);
	        out.write(text);
	        out.close();

	        return new File(fis);
	    }

	    
	    
	    private final static Porter porter = new Porter();
	    public static String toCanonicalForm(String word) {
	        return porter.stripAffixes(word);
	    }
	    
	    // citesc din fisier litera cu litera si returnez lista de cuvinte
	    private HashMap<String, Integer> citirefisier(String fileName) throws IOException
	    {
	    	// se creeaza conexiunea cu baza de date MongoDB
			//DBManager con = new DBManager("RIW");
			// se creeaza colectia pentru indexul direct
			//con.createCollection("direct");
			
	        HashMap<String, Integer> lista = new HashMap<String, Integer>();
	        // citesc din fisier litera cu litera
	        FileReader fis = null;
	        fis = new FileReader(fileName);
	        StringBuilder sb = new StringBuilder();
	        int litera;  
	        while ((litera = fis.read()) != -1)
	        {
	            
	            if (!Character.isLetterOrDigit((char)litera)) // suntem pe un separator
	            {
	                String cuvant = sb.toString(); // cream cuvantul nou           
	                // daca este exceptie
	                if (exceptii.exceptions.contains(cuvant))
	                {
	                    // il adaug
	                    if (lista.containsKey(cuvant))
	                    {
	                    	lista.put(cuvant, lista.get(cuvant) + 1);
	                    	//con.Actualizare(fileName, cuvant);
 	 
	                    } 
	                    else // daca nu, il adaug
	                    {
	                    	lista.put(cuvant, 1);
	                    	//con.Actualizare(fileName, cuvant);	
	                    }
	                }
	                else // cuvant de dictionar
	                {
	                	 if (!stopwords.stopwords.contains(cuvant))
	                	 { 
	                		 String word = toCanonicalForm(cuvant);
	                		 if (lista.containsKey(word)) 
	                		 {
	                			 lista.put(word, lista.get(word) + 1);
	                			 //con.Actualizare(fileName, word);	
	                		 } 
	                		 else // daca nu, il adaug
	                		 {
	                			 lista.put(word, 1);
	                			 //con.Actualizare(fileName, word);
	
	                		 }
	                	 }
	                	 else
	                	 {
	                		 // il ignor
	 	                    sb.setLength(0);
	 	                    continue;
	                	 }
	                }
	                sb.setLength(0); // golesc StringBuilder-ul
	            }
	            else // suntem in mijlocul unui cuvant
	            {
	                sb.append((char)litera); 
	            }
	        }

	        // sterg cuvantul vid
	        lista.remove("");

	        // fac afisarea in fisier(cuvant+ nr aparitii)
	        StringBuilder directi = new StringBuilder(fileName);

	        // fisierul va avea extensia ".directindex.json" adaugata numelui original al fisierului HTML
	        directi.replace(directi.lastIndexOf(".") + 1, directi.length(), "IndexDirect.json");
	        directi.replace(directi.lastIndexOf(".") + 1, directi.length(), "IndexDirect.txt");//+txt
	        Writer writer = new BufferedWriter(new OutputStreamWriter(
	                new FileOutputStream(directi.toString()), "utf-8"));

	        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
	        String jsonFile = gsonBuilder.toJson(lista);

	        writer.write(jsonFile);
	        writer.close();
	        fis.close(); 
	        return lista;
	    }

	    // calculez indexul direct
	    public HashMap<String, HashMap<String, Integer>> directIndex() throws IOException
	    {
	        HashMap<String, HashMap<String, Integer>> directIndex = new HashMap<>();
	        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
	        LinkedList<String> coadaDirectoare = new LinkedList<>();
	        coadaDirectoare.add(folder);//pun in coada "director"
	        while (!coadaDirectoare.isEmpty()) 
	        {
	            // iau un folder din coada
	            String directorCurent = coadaDirectoare.pop();
	            File folder = new File(directorCurent);
	            File[] listOfFiles = folder.listFiles();
	            // parcurg fisierele
	            try {
	                for (File file : listOfFiles)
	                {
	                	if (file.isDirectory()) // daca intalnesc un alt folder il pun in ccoada
	                    {
	                    	coadaDirectoare.add(file.getAbsolutePath());
	                    }
	                    // daca intalnesc un fisier verfic daca e html
	                	else if (file.isFile() && Files.probeContentType(file.toPath()).equals("text/html"))
	                    {
	                        // parsez fisierul HTML folosind JSOUP
	                        Document doc = Jsoup.parse(file, null);
	                        String fileName = file.getAbsolutePath();
	                        
	                        // fac un fisier separat pentru text
	                        File textFile = lab1(doc, file);
	                        String textFileName = textFile.getAbsolutePath();

	                        //aplic functia de citirefisier si o sa rezulte un hash (cuvant: numar aparitii) si afisez in fisierul ...indexdirect
	                        HashMap<String, Integer> currentDocWords = citirefisier(textFileName);

	                        // adaug documentul si cuvintele in hashul final
	                        directIndex.put(fileName, currentDocWords);
	                        System.out.println("Index direct pentru fisierul \"" + textFileName + "\".");
	                    }
	                  
	                }
	            } catch (NullPointerException e) {
	                System.out.println("Nu exista fisiere in folderul \"" + directorCurent + "\"!");
	            	
	            }
	        }
	        return directIndex;
	    }

	    public  HashMap<String, HashMap<String, Integer>> indirectIndex() throws IOException
	    {
	    	 HashMap<String, HashMap<String, Integer>> indirectIndex = new  HashMap<>();
	        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
	        LinkedList<String> coadaDirectoare = new LinkedList<>();
	        coadaDirectoare.add(folder);

	        while (!coadaDirectoare.isEmpty()) 
	        {
	            String currentFolder = coadaDirectoare.pop();
	            File folder = new File(currentFolder);
	            File[] listOfFiles = folder.listFiles();
	            // ii parcurgem lista de fisiere / foldere
	            try {
	                for (File file : listOfFiles)
	                {
	                	if (file.isDirectory()) // daca este folder, il pun in coada
	                    {
	                    	coadaDirectoare.add(file.getAbsolutePath());
	                    }
	                    // verific ca fisierulsa fie de tip index direct, creat anterior
	                	else if (file.isFile() && file.getAbsolutePath().endsWith(".IndexDirect.json"))
	                    {
	                        String fisier = file.getAbsolutePath();
	                        String docName = fisier.replace(".IndexDirect.json", "");

	                        // iau fisierul JSON cu indexul direct si il parsez
	                        Type directIndexType = new TypeToken<HashMap<String, Integer>>(){}.getType();
	                        HashMap<String, Integer> directIndex = gsonBuilder.fromJson(new String(Files.readAllBytes(file.toPath())), directIndexType);
	                        for(Map.Entry<String, Integer> entry : directIndex.entrySet()) // luam fiecare cuvant si stocam numarul de aparitii si documentul din care face parte
	                        {
	                            String cuvant = entry.getKey();
	                            int nr_aparitii = entry.getValue();
	                            // adaug intrarea in hash
	                            if (indirectIndex.containsKey(cuvant)) // daca acel cuvant exista in hash
	                            {
	                                // il adaug
	                                HashMap<String, Integer> aparitii = indirectIndex.get(cuvant);
	                                aparitii.put(docName, nr_aparitii);
	                            }
	                            else
	                            {
	                                HashMap<String, Integer> aparitii = new HashMap<>();
	                                aparitii.put(docName, nr_aparitii);
	                                indirectIndex.put(cuvant, aparitii);
	                            }
	                        }      
	                    }
	                    
	                }
	            } catch (NullPointerException e) {
	                System.out.println("Nu exista fisiere in folderul \"" + currentFolder + "\"!");
	            }
	        }
	        // fac fisierul concatenat de index indirect
	        Writer indirectIndexWriter = new BufferedWriter(new OutputStreamWriter(
	                new FileOutputStream(folder + "IndexIndirect.json"), "utf-8"));
	        
	        
	        indirectIndexWriter.write(gsonBuilder.toJson(indirectIndex));
	        indirectIndexWriter.close(); 
	        System.out.println("\nFisierul cu indexul indirect este \"" + folder + "IndexIndirect.map\".");

	        return indirectIndex;
	    }
	    
	   
}
