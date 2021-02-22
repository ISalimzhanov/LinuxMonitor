package database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class ProcessTabDao {
    private static final String DBNAME = "proc_db";
    private static final String COLLECTION_NAME = "windows";

    // Save data about one Process in db
    public static void saveWindow(ProcessTab processTab, String date) throws UnknownHostException {
        Document doc = new Document()
                .append(ProcessTabProperties.NAME.name(), processTab.getName())
                .append(ProcessTabProperties.STATUS.name(), processTab.getStatus())
                .append(ProcessTabProperties.START.name(), processTab.getStart());
        MongoDatabase db = DatabaseManager.getInstance(DBNAME);
        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME.concat(date));
        collection.insertOne(doc);
    }

    // Get all processes as ArrayList of Processes
    public static ArrayList<ProcessTab> getAllWindows(String date) throws UnknownHostException {
        System.out.println("getting all windows");
        MongoDatabase db = DatabaseManager.getInstance(DBNAME);
        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME.concat(date));
        MongoCursor<Document> cursor = collection.find().iterator();
        ArrayList<ProcessTab> windowTabs = new ArrayList<>();
        try {
            while (cursor.hasNext()){
                Document doc = cursor.next();
                windowTabs.add(
                        new ProcessTab(
                                doc.getString(ProcessTabProperties.NAME.name()),
                                doc.getString(ProcessTabProperties.STATUS.name()),
                                doc.getString(ProcessTabProperties.START.name())
                        )
                );
            }
        } finally {
            cursor.close();
        }
        return windowTabs;
    }
}
