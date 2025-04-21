package model.repository;

import model.Pharmacy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyRepository {

    private final Connection connection;

    public PharmacyRepository() {
        this.connection = Repository.getInstance().getConnection();
    }

    public void addPharmacy(Pharmacy pharmacy) {
        String sql = "INSERT INTO pharmacy(name, address) VALUES (?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, pharmacy.getName());
            statement.setString(2, pharmacy.getAddress());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                pharmacy.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePharmacy(int id) {
        String sql = "DELETE FROM pharmacy where id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePharmacy(Pharmacy pharmacy) {
        String sql = "UPDATE pharmacy SET name = ?, address = ? WHERE id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, pharmacy.getName());
            statement.setString(2, pharmacy.getAddress());
            statement.setInt(3, pharmacy.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pharmacy> getAllPharmacies() {
        List<Pharmacy> pharmacies = new ArrayList<>();

        String sql = "SELECT * FROM pharmacy";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Pharmacy pharmacy = new Pharmacy();
                pharmacy.setId(resultSet.getInt("id"));
                pharmacy.setName(resultSet.getString("name"));
                pharmacy.setAddress(resultSet.getString("address"));
                pharmacies.add(pharmacy);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pharmacies;
    }

}
