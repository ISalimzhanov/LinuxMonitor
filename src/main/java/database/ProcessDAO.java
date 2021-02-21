package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;


// Class for accessing local database
public class ProcessDAO {
    private static final String DBNAME = "proc_db";
    private static final String COLLECTION_NAME = "processes";

    // Save data about one Process in db
    public static void saveProcess(ProcessRecord processRecord, String date) throws UnknownHostException {
        Document doc = new Document()
                .append(ProcessProperties.NAME.name(), processRecord.getName())
                .append(ProcessProperties.CPU.name(), processRecord.getCpuUsage())
                .append(ProcessProperties.TIME.name(), processRecord.getWorkTime())
                .append(ProcessProperties.USER.name(), processRecord.getUser())
                .append(ProcessProperties.MEMORY_UTIL.name(), processRecord.getMemoryUtil())
                .append(ProcessProperties.STAT.name(), processRecord.getProcStat())
                .append(ProcessProperties.RSS.name(), processRecord.getProcRSS())
                .append(ProcessProperties.TTY.name(), processRecord.getProcTTY())
                .append(ProcessProperties.PID.name(), processRecord.getPid())
                .append(ProcessProperties.START.name(), processRecord.getStartTime())
                .append(ProcessProperties.VSZ.name(), processRecord.getProcVSZ())
                .append(ProcessProperties.COMMAND.name(), processRecord.getCommand());
        MongoDatabase db = DatabaseManager.getInstance(DBNAME);
        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME.concat(date));
        collection.insertOne(doc);
    }

    // Get all processes as ArrayList of Processes
    public static ArrayList<ProcessRecord> getAllProcesses(String date) throws UnknownHostException {
        MongoDatabase db = DatabaseManager.getInstance(DBNAME);
        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME.concat(date));
        MongoCursor<Document> cursor = collection.find().iterator();
        ArrayList<ProcessRecord> processRecords = new ArrayList<>();
        try {
            while (cursor.hasNext()){
                Document doc = cursor.next();
                processRecords.add(
                        new ProcessRecord(
                            doc.getString(ProcessProperties.NAME.name()),
                            doc.getString(ProcessProperties.CPU.name()),
                            doc.getString(ProcessProperties.MEMORY_UTIL.name()),
                            doc.getString(ProcessProperties.TIME.name()),
                            doc.getString(ProcessProperties.USER.name()),
                            doc.getString(ProcessProperties.STAT.name()),
                            doc.getString(ProcessProperties.RSS.name()),
                            doc.getString(ProcessProperties.TTY.name()),
                            doc.getString(ProcessProperties.PID.name()),
                            doc.getString(ProcessProperties.START.name()),
                            doc.getString(ProcessProperties.VSZ.name()),
                            doc.getString(ProcessProperties.COMMAND.name())
                        )
                );
            }
        } finally {
            cursor.close();
        }
        return processRecords;
    }
}
