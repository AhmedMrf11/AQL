package org.example.partie02.exercice2;

import org.example.partie01.exercice2.Order;
import org.example.partie01.exercice2.OrderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDaoJdbcImpl implements OrderDao {
    private final Connection connection;

    public OrderDaoJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveOrder(Order order) {
        String sql = "INSERT INTO orders (id) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving order", e);
        }
    }
}
