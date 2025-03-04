package org.example.currency_exchange.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    public T create(T t);
    public T update(T t);
    public T delete(T t);
    public List<T> getAll() throws SQLException;
    public Optional<T> getById(int id);
}
