package com.openspaceservices.user.app.repository;

import com.openspaceservices.user.app.model.DataSourceConfig;
import org.springframework.data.repository.CrudRepository;


public interface DataSourceConfigRepository extends CrudRepository<DataSourceConfig, Long> {

    public DataSourceConfig findByNameAndInitialize(String name ,Boolean initialize);

}
