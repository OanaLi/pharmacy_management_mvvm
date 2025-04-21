package model.database;

import model.repository.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLTableCreationFactory {

    private final Connection connection;

    public SQLTableCreationFactory(Repository repository) {
        connection = repository.getConnection();
    }

    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS pharmacy(" +
                " id bigint NOT NULL AUTO_INCREMENT," +
                " `name` VARCHAR(500) NOT NULL," +
                " address VARCHAR(500) NOT NULL," +
                " PRIMARY KEY (id)," +
                " UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS medicament(" +
                " id bigint NOT NULL AUTO_INCREMENT," +
                " `name` VARCHAR(500) NOT NULL," +
                " producer VARCHAR(500) NOT NULL," +
                " price DECIMAL(10, 2) NOT NULL," +
                " image VARCHAR(1000)," +
                " PRIMARY KEY (id)," +
                " UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS pharmacy_medicaments(" +
                " id_pharmacy BIGINT NOT NULL," +
                " id_medicament BIGINT NOT NULL," +
                " stock INT NOT NULL," +
                " expiration_date DATE NOT NULL," +
                " PRIMARY KEY (id_pharmacy, id_medicament)," +
                " CONSTRAINT fk_pharmacy FOREIGN KEY (id_pharmacy) REFERENCES pharmacy(id) ON DELETE CASCADE," +
                " CONSTRAINT fk_medicament_pharmacy FOREIGN KEY (id_medicament) REFERENCES medicament(id) ON DELETE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        statement.execute(sql);
        statement.close();
    }

    public static void main(String[] args) {
        try {
            Repository repository = Repository.getInstance();
            SQLTableCreationFactory factory = new SQLTableCreationFactory(repository);

            factory.createTables();

            System.out.println("Bootstrap finished!");
        } catch (SQLException e) {
            System.err.println("Error while creating tables!");
            e.printStackTrace();
        }
    }
}
