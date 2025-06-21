package org.yearup.repository.mysql;

import org.springframework.stereotype.Component;
import org.yearup.model.Order;
import org.yearup.model.OrderLineItem;
import org.yearup.model.Product;
import org.yearup.repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDao extends DataManager implements OrderRepository {
    private final ProductDao productDao;

    public OrderDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public Order create(Order order) {
        String sql = """
                INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            ps.setString(3, order.getAddress());
            ps.setString(4, order.getCity());
            ps.setString(5, order.getState());
            ps.setString(6, order.getZip());
            ps.setBigDecimal(7, order.getShippingAmount());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return getById(id);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error creating order", ex);
        }
        return null;
    }

    @Override
    public void createLineItem(OrderLineItem item) {
        String sql = """
                INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount, date)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setBigDecimal(3, item.getSalesPrice());
            ps.setInt(4, item.getQuantity());
            ps.setBigDecimal(5, item.getDiscount());
            ps.setTimestamp(6, Timestamp.valueOf(item.getDate()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error creating line item", ex);
        }
    }

    @Override
    public List<Order> listByUserId(int userId) {
        String sql = """
                SELECT o.*, IFNULL(SUM(li.sales_price * li.quantity - li.discount),0) AS total
                FROM orders o
                LEFT JOIN order_line_items li ON o.order_id = li.order_id
                WHERE o.user_id = ?
                GROUP BY o.order_id
                ORDER BY o.date DESC
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
                return orders;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error listing orders", ex);
        }
    }

    @Override
    public Order getById(int orderId) {
        String sql = """
                SELECT o.*, IFNULL(SUM(li.sales_price * li.quantity - li.discount),0) AS total
                FROM orders o
                LEFT JOIN order_line_items li ON o.order_id = li.order_id
                WHERE o.order_id = ?
                GROUP BY o.order_id
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error fetching order", ex);
        }
        return null;
    }

    @Override
    public List<OrderLineItem> getLineItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_line_items WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrderLineItem> items = new ArrayList<>();
                while (rs.next()) {
                    OrderLineItem item = new OrderLineItem();
                    item.setOrderLineItemId(rs.getInt("order_line_item_id"));
                    item.setOrderId(rs.getInt("order_id"));
                    int pid = rs.getInt("product_id");
                    item.setProductId(pid);
                    item.setSalesPrice(rs.getBigDecimal("sales_price"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setDiscount(rs.getBigDecimal("discount"));
                    item.setDate(rs.getTimestamp("date").toLocalDateTime());
                    Product product = productDao.getById(pid);
                    item.setProduct(product);
                    items.add(item);
                }
                return items;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error fetching line items", ex);
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setDate(rs.getTimestamp("date").toLocalDateTime());
        order.setAddress(rs.getString("address"));
        order.setCity(rs.getString("city"));
        order.setState(rs.getString("state"));
        order.setZip(rs.getString("zip"));
        order.setShippingAmount(rs.getBigDecimal("shipping_amount"));
        order.setTotal(rs.getBigDecimal("total"));
        return order;
    }
}