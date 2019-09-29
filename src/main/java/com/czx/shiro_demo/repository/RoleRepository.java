package com.czx.shiro_demo.repository;


import com.czx.shiro_demo.entity.RoleEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色
 */
@Repository
public interface RoleRepository extends MongoRepository<RoleEntity, ObjectId> {
    RoleEntity findByName(String name);
}
