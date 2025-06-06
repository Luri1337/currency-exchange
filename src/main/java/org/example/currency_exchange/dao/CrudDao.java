package org.example.currency_exchange.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    void create(T t);

    void update(T t) throws SQLException;

    List<T> getAll() throws SQLException;

    Optional<T> getById(int id);
}
