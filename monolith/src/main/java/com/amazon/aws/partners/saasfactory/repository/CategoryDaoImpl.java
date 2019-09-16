package com.amazon.aws.partners.saasfactory.repository;

import com.amazon.aws.partners.saasfactory.domain.Category;
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
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Category getCategory(Long categoryId) throws Exception {
        return jdbc.queryForObject("SELECT category_id, category FROM category WHERE category_id = ?", new Object[]{categoryId}, new CategoryRowMapper());
    }

    @Override
    public List<Category> getCategories() throws Exception {
        return jdbc.query("SELECT category_id, category FROM category", new CategoryRowMapper());
    }

    @Override
    public Category saveCategory(Category category) throws Exception {
        if (category.getId() != null && category.getId() > 0) {
            return updateCategory(category);
        } else {
            return insertCategory(category);
        }
    }

    private Category insertCategory(Category category) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO category (category) VALUES (?)" );
            ps.setString(1, category.getName());
            return ps;
        }, keyHolder);
        category.setId(keyHolder.getKey().longValue());
        return category;
    }

    private Category updateCategory(Category category) throws Exception {
        jdbc.update("UPDATE category SET category = ? WHERE category_id = ?", new Object[]{category.getName(), category.getId()});
        return category;
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        jdbc.update("DELETE FROM category WHERE category_id = ?", new Object[]{category.getId()});
    }

    class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet result, int rowMapper) throws SQLException {
            return new Category(result.getLong("category_id"), result.getString("category"));
        }
    }
}
