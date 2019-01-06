package dbUtil;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.scene.control.Tab;
import model.*;

import java.io.IOException;
import java.sql.SQLException;

public class dbSqlite {

    private final static String DATABASE_URL = "jdbc:sqlite:C:/db/baza1.db";
    private static ConnectionSource connectionSource;
    private static Dao<userModel, Integer> userModelDao;

    public static void createConnectionSource(){
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            System.out.println("Udało się połączyć z bazą danych");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ConnectionSource getConnectionSource(){
        if(connectionSource == null){
            createConnectionSource();
        }
        return connectionSource;
    }
    public static void closeConnection(){
        if(connectionSource != null){
            try {
                connectionSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void initDatabase(){

        try {
            //userModelDao = DaoManager.createDao(dbSqlite.getConnectionSource(), userModel.class);
            //TableUtils.dropTable(dbSqlite.getConnectionSource(), clientModel.class,true);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), unitModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
     /*   typDao = DaoManager.createDao(dbSqlite.getConnectionSource(), przyrzadTyp.class);
        nazwaDao = DaoManager.createDao(dbSqlite.getConnectionSource(), przyrzadNazwa.class);
        zakresDao = DaoManager.createDao(dbSqlite.getConnectionSource(), przyrzadZakres.class);


        TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), przyrzadNazwa.class);
        TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), przyrzadZakres.class);
        TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), przyrzad.class);*/
    }

}