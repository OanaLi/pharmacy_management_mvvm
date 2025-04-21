package model.repository;

import model.Medicament;
import model.Producer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentRepository {

    private final Connection connection;

    public MedicamentRepository() {
        this.connection = Repository.getInstance().getConnection();
    }


    public void addMedicament(Medicament medicament) {
        String sql = "INSERT INTO medicament(name, producer, price, image) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, medicament.getName());
            statement.setString(2, medicament.getProducer());
            statement.setFloat(3, medicament.getPrice());
            statement.setString(4, medicament.getImage());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                medicament.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMedicament(int id) {
        String sql = "DELETE FROM medicament WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateMedicament(Medicament medicament) {
        String sql = "UPDATE medicament SET name = ?, price = ?, image = ?, producer = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, medicament.getName());
            statement.setFloat(2, medicament.getPrice());
            statement.setString(3, medicament.getImage());
            statement.setString(4, medicament.getProducer());
            statement.setInt(5, medicament.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Medicament getMedicamentById(int id) {
        String sql = "SELECT * FROM medicament WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Medicament medicament = mapResultSetToMedicament(resultSet);
                    return medicament;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //VIZUALIZARE ALFABETICA
    public List<Medicament> getAllMedicaments() {
        List<Medicament> medicaments = new ArrayList<>();

        String sql = "SELECT * FROM medicament ORDER BY name ASC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Medicament medicament = mapResultSetToMedicament(resultSet);
                medicaments.add(medicament);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }


    //CAUTARE DUPA NUME
    public List<Medicament> searchMedicamentsByName(String name) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament WHERE name LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                medicaments.add(mapResultSetToMedicament(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }


    //HELPER
    private Medicament mapResultSetToMedicament(ResultSet resultSet) throws SQLException {
        return new Medicament(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                Producer.valueOf(resultSet.getString("producer")),
                resultSet.getFloat("price"),
                resultSet.getString("image")
        );
    }


}
