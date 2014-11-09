package de.rwth.dbis.layers.lapps.data;

import java.util.List;

import de.rwth.dbis.layers.lapps.entity.Entity;

public interface Dao<T extends Entity, I> {
  List<T> findAll();

  T find(I id);

  T save(T entity);
}
