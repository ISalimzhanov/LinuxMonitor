package database;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;

// singleton class for database
public class DatabaseManager {

    private static MongoDatabase instance;

    private DatabaseManager() {
    }

    public static MongoDatabase getInstance(String dbName)
            throws UnknownHostException {
        if (instance == null) {
            instance = MongoClients.create().getDatabase(dbName);
        }

        return instance;
    }

}
