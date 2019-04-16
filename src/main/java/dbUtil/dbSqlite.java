package dbUtil;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.*;
import util.showAlert;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Klasa zawiera metody do obsługi bazy danych.
 */
public class dbSqlite {
    private final static String DATABASE_URL = "jdbc:sqlite:C:/db/baza1.db";
    private static ConnectionSource connectionSource;
    private static Dao<userModel, Integer> userModelDao;

    /**
     * Metoda służąca do nawiązywania połączenia z bazą danych
     */
    public static void createConnectionSource(){
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }

    /**
     * Metoda zwraca obiekt ConnectionSource, połączenie do bazy danych
     * @return ConnectionSource
     */
    public static ConnectionSource getConnectionSource(){
        if(connectionSource == null){
            createConnectionSource();
        }
        return connectionSource;
    }
    /**
     * Metoda służąca do zamykania połączenia z bazą danych
     */
    public static void closeConnection(){
        if(connectionSource != null){
            try {
                connectionSource.close();
            } catch (IOException e) {
                showAlert.display(e.getMessage());
            }
        }
    }

    /**
     * Metoda do inicjalizacji bazy danych. Tworzenia nowych tabel itd
     */
    public static void initDatabase(){

        try {
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), clientModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), instrumentModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), instrumentNameModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), instrumentProducerModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), instrumentTypeModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), register2Model.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), registerModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), storehouseModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), unitModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), userModel.class);
            TableUtils.createTableIfNotExists(dbSqlite.getConnectionSource(), yearModel.class);

        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }

}