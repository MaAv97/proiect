package proiect;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class DBManager {

	String dbName;
	MongoClient mongo;
	MongoDatabase db;
	MongoCollection<Document> c;

	
	
	public DBManager(String dbName)
	{
		mongo = new MongoClient( "localhost" , 27017 );
		this.dbName = dbName;
		MongoCredential credential;
		credential = MongoCredential.createCredential("sampleUser", dbName, "password".toCharArray());
		db = getDB();
		System.out.println("Connected to the database successfully");	
	}
	
	public String getName()
	{
		return this.dbName;
	}
	
	public MongoDatabase getDB()
	{
		this.db = mongo.getDatabase(this.dbName);
		return db;
	}
	
	public void createCollection(String coll)
	{
		db.createCollection(coll);
	}
	
	public void setCollection(String collName)
	{
		this.c = db.getCollection(collName);
	}
	
	public void Actualizare (String numeFisier, String cuvant)
	{
		
		Bson filter = Filters.eq("fisier", numeFisier);
		Bson update = new Document("$inc", new Document().append("cuvinte."+cuvant,1));
		UpdateOptions options = new UpdateOptions().upsert(true);
		
		c.updateOne(filter, update, options);
	}
}
