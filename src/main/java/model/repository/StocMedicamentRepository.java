package model.repository;

import model.StocMedicament;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StocMedicamentRepository {

    private final Connection connection;

    public StocMedicamentRepository() {
        this.connection = Repository.getInstance().getConnection();
    }


    public void addStocMedicament(StocMedicament stocMedicament) {
        String sql = "INSERT INTO pharmacy_medicaments(id_pharmacy, id_medicament, stock, expiration_date) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, stocMedicament.getPharmacyId());
            statement.setInt(2, stocMedicament.getMedicamentId());
            statement.setInt(3, stocMedicament.getStock());
            statement.setDate(4, java.sql.Date.valueOf(stocMedicament.getExpirationDate()));
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStocMedicament(int pharmacyId, int medicamentId) {
        String sql = "DELETE FROM pharmacy_medicaments WHERE id_pharmacy = ? AND  id_medicament = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pharmacyId);
            statement.setInt(2, medicamentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateStocMedicament(StocMedicament stocMedicament) {
        String sql = "UPDATE pharmacy_medicaments SET stock = ?, expiration_date = ? WHERE id_pharmacy = ? AND id_medicament = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, stocMedicament.getStock());
            statement.setDate(2, java.sql.Date.valueOf(stocMedicament.getExpirationDate()));
            statement.setInt(3, stocMedicament.getPharmacyId());
            statement.setInt(4, stocMedicament.getMedicamentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ///////////FILTRARE//////////

    //DUPA DISPONIBILITATE
    public List<StocMedicament> getAvailableMedicaments(int pharmacyId) {
        List<StocMedicament> stocMedicaments = new ArrayList<>();
        String sql = "SELECT m.id AS id_medicament, m.name, m.producer, m.price, m.image, " +
                "pm.id_pharmacy, pm.stock, pm.expiration_date " +
                "FROM medicament m " +
                "INNER JOIN pharmacy_medicaments pm ON m.id = pm.id_medicament " +
                "WHERE pm.stock > 0 AND pm.id_pharmacy = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pharmacyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                stocMedicaments.add(mapResultSetToStocMedicament(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocMedicaments;
    }



    //DUPA VALABILITATE
    public List<StocMedicament> getValidMedicaments(int pharmacyId) {
        List<StocMedicament> stocMedicaments = new ArrayList<>();

        String sql = "SELECT m.id AS id_medicament, m.name, m.producer, m.price, m.image, " +
                "pm.id_pharmacy, pm.stock, pm.expiration_date " +
                "FROM medicament m " +
                "JOIN pharmacy_medicaments pm ON m.id = pm.id_medicament " +
                "WHERE pm.expiration_date > CURRENT_DATE AND pm.id_pharmacy = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pharmacyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                stocMedicaments.add(mapResultSetToStocMedicament(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocMedicaments;
    }



    //DUPA PRODUCER
    public List<StocMedicament> getMedicamentsByProducer(int pharmacyId, String producer) {
        List<StocMedicament> stocMedicaments = new ArrayList<>();
        String sql = "SELECT m.id AS id_medicament, m.name, m.producer, m.price, m.image, " +
                "pm.id_pharmacy, pm.stock, pm.expiration_date " +
                "FROM medicament m " +
                "JOIN pharmacy_medicaments pm ON m.id = pm.id_medicament " +
                "WHERE m.producer = ? AND pm.id_pharmacy = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, producer);
            statement.setInt(2, pharmacyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                stocMedicaments.add(mapResultSetToStocMedicament(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocMedicaments;
    }



    public List<StocMedicament> getAllPharmacyMedicaments(int pharmacyId) {
        List<StocMedicament> stocMedicaments = new ArrayList<>();
        String sql = "SELECT m.id AS id_medicament, m.name, m.producer, m.price, m.image, " +
                "pm.stock, pm.expiration_date " +
                "FROM medicament m " +
                "JOIN pharmacy_medicaments pm ON m.id = pm.id_medicament " +
                "WHERE pm.id_pharmacy = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pharmacyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                stocMedicaments.add(new StocMedicament(
                        pharmacyId,
                        resultSet.getInt("id_medicament"),
                        resultSet.getString("name"),
                        resultSet.getString("producer"),
                        resultSet.getFloat("price"),
                        resultSet.getString("image"),
                        resultSet.getInt("stock"),
                        resultSet.getDate("expiration_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stocMedicaments;
    }


    //HELPER
    private StocMedicament mapResultSetToStocMedicament(ResultSet resultSet) throws SQLException {
        return new StocMedicament(
                resultSet.getInt("id_pharmacy"),
                resultSet.getInt("id_medicament"),
                resultSet.getString("name"),
                resultSet.getString("producer"),
                resultSet.getFloat("price"),
                resultSet.getString("image"),
                resultSet.getInt("stock"),
                resultSet.getDate("expiration_date").toLocalDate()
        );

    }



}