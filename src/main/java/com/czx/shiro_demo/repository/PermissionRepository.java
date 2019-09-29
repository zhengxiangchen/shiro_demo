package com.czx.shiro_demo.repository;


import com.czx.shiro_demo.entity.PermissionEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 权限mongo
 */
@Repository
public interface PermissionRepository extends MongoRepository<PermissionEntity, ObjectId> {
    PermissionEntity findByName(String name);
}
