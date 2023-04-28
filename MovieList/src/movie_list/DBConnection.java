package movie_list;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static volatile Connection dbConnection;

    public static Connection getDBConnection(){
        if(dbConnection == null){
            synchronized(DBConnection.class){
                if(dbConnection == null){
                    try{
                        Class.forName("org.sqlite.JDBC");
                        dbConnection = DriverManager.getConnection("jdbc:sqlite:db/movielist.db");
                    } catch(SQLException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return dbConnection;
    }

    public static void closeDBConnection(){
        try{
            dbConnection.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
