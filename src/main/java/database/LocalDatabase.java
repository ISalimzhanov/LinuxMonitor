package database;


public class LocalDatabase {

    private static LocalDatabase instance;

    private LocalDatabase(){}

    public static LocalDatabase getInstance(){
        if (instance == null){
            instance = new LocalDatabase();
        }

        return instance;
    }

    public void addData(){

    }

    public int getData(){
        return 0;
    }

}
