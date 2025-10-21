package com.mjc.school.controller;

import java.util.List;

public interface BaseController<T, R, K> {

    List<R> findAll();

    R findById(K id);

    R create(T createRequest);

    R update(T updateRequest);

    boolean deleteById(K id);

}
