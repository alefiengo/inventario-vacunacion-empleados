package com.alefiengo.inventariovacunacionempleados.service;

import java.util.Optional;

public interface GenericService<E> {

    Optional<E> findById(Long id);

    E save(E entity);

    Iterable<E> findAll();

    void deleteById(Long id);
}