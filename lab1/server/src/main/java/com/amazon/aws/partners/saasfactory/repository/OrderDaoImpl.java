package com.amazon.aws.partners.saasfactory.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.RowMapper;

import com.amazon.aws.partners.saasfactory.domain.*;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
    private final static String SELECT_ORDER_SQL = "SELECT o.order_fulfillment_id, o.order_date, o.ship_date, " +
            "p.purchaser_id, p.first_name, p.last_name, " +
            "o.ship_to_line1, o.ship_to_line2, o.ship_to_city, o.ship_to_state, o.ship_to_postal_code, " +
            "o.bill_to_line1, o.bill_to_line2, o.bill_to_city, o.bill_to_state, o.bill_to_postal_code " +
            "FROM order_fulfillment o " +
            "INNER JOIN purchaser p ON o.purchaser_id = p.purchaser_id";
    private final static String INSERT_ORDER_SQL = "INSERT INTO order_fulfillment (order_date, ship_date, " +
            "purchaser_id, ship_to_line1, ship_to_line2, ship_to_city, ship_to_state, ship_to_postal_code, " +
            "bill_to_line1, bill_to_line2, bill_to_city, bill_to_state, bill_to_postal_code) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_ORDER_SQL = "UPDATE order_fulfillment SET order_date = ?, ship_date = ?, purchaser_id = ?, " +
            "ship_to_line1 = ?, ship_to_line2 = ?, ship_to_city = ?, ship_to_state = ?, ship_to_postal_code = ?, " +
            "bill_to_line1 = ?, bill_to_line2 = ?, bill_to_city = ?, bill_to_state = ?, bill_to_postal_code = ? " +
            "WHERE order_fulfillment_id = ?";
    private final static String DELETE_ORDER_SQL = "DELETE FROM order_fulfillment WHERE order_fulfillment_id = ?";
    private final static String DELETE_ORDER_LINE_ITEMS_SQL = "DELETE FROM order_line_item WHERE order_fulfillment_id = ?";

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrder(Integer orderId) throws Exception {
        String sql = SELECT_ORDER_SQL.concat(" WHERE order_fulfillment_id = ?");
        return jdbc.queryForObject(sql, new Object[]{orderId}, new OrderRowMapper());
    }

    @Override
    public List<Order> getOrders() throws Exception {
        return jdbc.query(SELECT_ORDER_SQL, new OrderRowMapper());
    }

    @Override
    public Order saveOrder(Order order) throws Exception {
        if (order.getId() != null && order.getId() > 0) {
            return updateOrder(order);
        } else {
            return insertOrder(order);
        }
    }

    private Order insertOrder(Order order) throws Exception {
        logger.info("OrderDao::insertOrder " + order);
        // First, make sure we have an updated purchaser
        Purchaser purchaser = order.getPurchaser();
        Integer purchaserId = null;
        if (purchaser != null) {
            purchaserId = purchaser.getId();
            if (purchaserId == null || purchaserId < 1) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbc.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO purchaser (first_name, last_name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, purchaser.getFirstName());
                    ps.setString(2, purchaser.getLastName());
                    return ps;
                }, keyHolder);
                if (!keyHolder.getKeys().isEmpty()) {
                    purchaser.setId((Integer) keyHolder.getKeys().get("purchaser_id"));
                } else {
                    purchaser.setId(keyHolder.getKey().intValue());
                }
            }
            order.setPurchaser(purchaser);
        }

        // Now insert the order
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_ORDER_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, order.getOrderDate() != null ? new java.sql.Date(order.getOrderDate().getTime()) : null);
            ps.setDate(2, order.getShipDate() != null ? new java.sql.Date(order.getShipDate().getTime()) : null);
            ps.setInt(3, order.getPurchaser() != null ? order.getPurchaser().getId() : null);
            Address shipAddress = order.getShipAddress();
            if (shipAddress == null) {
                shipAddress = new Address();
            }
            ps.setString(4, shipAddress.getLine1());
            ps.setString(5, shipAddress.getLine2());
            ps.setString(6, shipAddress.getCity());
            ps.setString(7, shipAddress.getState());
            ps.setString(8, shipAddress.getPostalCode());

            Address billAddress = order.getBillAddress();
            if (billAddress == null) {
                billAddress = new Address();
            }
            ps.setString(9, billAddress.getLine1());
            ps.setString(10, billAddress.getLine2());
            ps.setString(11, billAddress.getCity());
            ps.setString(12, billAddress.getState());
            ps.setString(13, billAddress.getPostalCode());

            return ps;
        }, keyHolder);
        if (!keyHolder.getKeys().isEmpty()) {
            order.setId((Integer) keyHolder.getKeys().get("order_fulfillment_id"));
        } else {
            order.setId(keyHolder.getKey().intValue());
        }

        // Now we can save the line items for this order
        List<OrderLineItem> lineItems = saveOrderLineItems(order.getId(), order.getLineItems());
        order.setLineItems(lineItems);

        return order;
    }

    private Order updateOrder(Order order) throws Exception {
        Purchaser purchaser = order.getPurchaser();
        if (purchaser == null) {
            purchaser = new Purchaser();
        }
        Address shipTo = order.getShipAddress();
        if (shipTo == null) {
            shipTo = new Address();
        }
        Address billTo = order.getBillAddress();
        if (billTo == null) {
            billTo = new Address();
        }
        jdbc.update(UPDATE_ORDER_SQL,
                order.getOrderDate(),
                order.getShipDate(),
                purchaser.getId(),
                shipTo.getLine1(),
                shipTo.getLine2(),
                shipTo.getCity(),
                shipTo.getState(),
                shipTo.getPostalCode(),
                billTo.getLine1(),
                billTo.getLine2(),
                billTo.getCity(),
                billTo.getState(),
                billTo.getPostalCode(),
                order.getId()
        );
        return order;
    }

    @Override
    public void deleteOrder(Order order) throws Exception {
        deleteOrderLineItems(order.getId());
        jdbc.update(DELETE_ORDER_SQL, new Object[]{order.getId()});
    }

    private List<OrderLineItem> getOrderLineItems(Integer orderId) throws Exception {
        String sql = "SELECT order_line_item_id, order_fulfillment_id, product_id, quantity, unit_purchase_price " +
                "FROM order_line_item WHERE order_fulfillment_id = ?";
        List<OrderLineItem> lineItems = jdbc.query(sql, new Object[]{orderId}, new OrderLineItemRowMapper());
        return lineItems;
    }

    private List<OrderLineItem> saveOrderLineItems(Integer orderId, List<OrderLineItem> lineItems) throws Exception {
        List<OrderLineItem> insertedItems = new ArrayList<>();
        for (OrderLineItem lineItem : lineItems) {
            lineItem.setOrderId(orderId);
            insertedItems.add(saveOrderLineItem(lineItem));
        }
        return insertedItems;
    }
    
    private void deleteOrderLineItems(Integer orderId) throws Exception {
        jdbc.update(DELETE_ORDER_LINE_ITEMS_SQL, orderId);
    }

    private OrderLineItem saveOrderLineItem(OrderLineItem lineItem) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO order_line_item (order_fulfillment_id, product_id, quantity, unit_purchase_price) " +
                "VALUES (?, ?, ?, ?)";
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, lineItem.getOrderId());
            ps.setInt(2, lineItem.getProduct().getId());
            ps.setInt(3, lineItem.getQuantity());
            ps.setBigDecimal(4, lineItem.getUnitPurchasePrice());
            return ps;
        }, keyHolder);
        if (!keyHolder.getKeys().isEmpty()) {
            lineItem.setId((Integer) keyHolder.getKeys().get("order_line_item_id"));
        } else {
            lineItem.setId(keyHolder.getKey().intValue());
        }
        return lineItem;
    }

    class OrderRowMapper  implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet result, int rowNumber) throws SQLException {
            Order order = new Order();
            order.setId(result.getInt("order_fulfillment_id"));
            order.setOrderDate(result.getDate("order_date"));
            order.setShipDate(result.getDate("ship_date"));
            order.setPurchaser(new Purchaser(
                    result.getInt("purchaser_id"),
                    result.getString("first_name"),
                    result.getString("last_name")
            ));
            order.setShipAddress(new Address(
                    result.getString("ship_to_line1"),
                    result.getString("ship_to_line2"),
                    result.getString("ship_to_city"),
                    result.getString("ship_to_state"),
                    result.getString("ship_to_postal_code")
            ));
            order.setBillAddress(new Address(
                    result.getString("bill_to_line1"),
                    result.getString("bill_to_line2"),
                    result.getString("bill_to_city"),
                    result.getString("bill_to_state"),
                    result.getString("bill_to_postal_code")
            ));
            try {
                order.setLineItems(getOrderLineItems(order.getId()));
            } catch (Exception e) {
                throw new SQLException(e);
            }
            return order;
        }
    }

    private final Map<Integer, Product> orderLineItemProductCache = new HashMap<>();
    class OrderLineItemRowMapper implements RowMapper<OrderLineItem> {
        @Override
        public OrderLineItem mapRow(ResultSet result, int rowNumber) throws SQLException {
            Integer productId = result.getInt("product_id");
            Product product = null;
            if (productId != null) {
                if (!orderLineItemProductCache.containsKey(productId)) {
                    try {
                        product = productDao.getProduct(productId);
                        orderLineItemProductCache.put(productId, product);
                    } catch (Exception e) {
                        throw new SQLException(e);
                    }
                } else {
                    product = orderLineItemProductCache.get(productId);
                }
            }
            OrderLineItem lineItem = new OrderLineItem(
                    result.getInt("order_line_item_id"),
                    result.getInt("order_fulfillment_id"),
                    product,
                    result.getInt("quantity"),
                    result.getBigDecimal("unit_purchase_price")
            );
            return lineItem;
        }
    }
}
