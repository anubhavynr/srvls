package com.amazon.aws.partners.saasfactory.repository;

import com.amazon.aws.partners.saasfactory.domain.Category;
import com.amazon.aws.partners.saasfactory.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Product getProduct(Long productId) throws Exception {
//        String sql = "SELECT p.product_id, p.sku, p.product, p.price FROM product p WHERE p.product_id = ?";
        String sql = "SELECT p.product_id, p.sku, p.product, p.price, c.category_id, c.category FROM product p JOIN (SELECT x.product_id, MAX(x.category_id) AS category_id FROM product_categories x JOIN product y ON x.product_id = y.product_id GROUP BY x.product_id) AS pc ON p.product_id = pc.product_id JOIN category AS c ON pc.category_id = c.category_id WHERE p.product_id = ?";
        return jdbc.queryForObject(sql, new Object[]{productId}, new ProductRowMapper());
    }

    @Override
    public List<Product> getProducts() throws Exception {
        return jdbc.query("SELECT p.product_id, p.sku, p.product, p.price, c.category_id, c.category FROM product p JOIN (SELECT x.product_id, MAX(x.category_id) AS category_id FROM product_categories x JOIN product y ON x.product_id = y.product_id GROUP BY x.product_id) AS pc ON p.product_id = pc.product_id JOIN category AS c ON pc.category_id = c.category_id", new ProductRowMapper());
    }

    @Override
    public Product saveProduct(Product product) throws Exception {
        if (product.getId() != null && product.getId() > 0) {
            return updateProduct(product);
        } else {
            return insertProduct(product);
        }
    }

    private Product insertProduct(Product product) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO product (sku, product, price) VALUES (?, ?, ?)" );
            ps.setString(1, product.getSku());
            ps.setString(2, product.getName());
            ps.setBigDecimal(3, product.getPrice());
            return ps;
        }, keyHolder);
        product.setId(keyHolder.getKey().longValue());
        return product;
    }

    private Product updateProduct(Product product) throws Exception {
        jdbc.update("UPDATE product SET sku = ?, product = ?, price = ? WHERE product_id = ?", new Object[]{product.getSku(), product.getName(), product.getPrice(), product.getId()});
        return product;
    }

    @Override
    public void deleteProduct(Product product) throws Exception {
        jdbc.update("DELETE FROM product WHERE product_id = ?", new Object[]{product.getId()});
    }

    class ProductRowMapper  implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet result, int rowNumber) throws SQLException {
            Product product = new Product(result.getLong("product_id"), result.getString("sku"), result.getString("product"), result.getBigDecimal("price"));
            Category category = new Category(result.getLong("category_id"), result.getString("category"));
            product.setCategory(category);
            return product;
        }
    }
}
