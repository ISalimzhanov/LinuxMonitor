package database;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.util.HashMap;

public class LocalDatabase  {

    private static MongoDatabase instance;

    private LocalDatabase(){}

    public static MongoDatabase getInstance(){
        if (instance == null){
            instance = MongoClients.create().getDatabase("procdb");
        }

        return instance;
    }

    public static void addData(HashMap<String, String> procDiary){
        Document doc = new Document("name", "process")
                .append("proc_name", procDiary.get("proc_name"))
                .append("cpu", procDiary.get("cpu"))
                .append("time", procDiary.get("time"));
        MongoCollection<Document> collection = instance.getCollection("processes");
        collection.insertOne(doc);
        System.out.println(instance.getCollection("processes").countDocuments());
    }

    public int getData(){
        return 0;
    }

}
