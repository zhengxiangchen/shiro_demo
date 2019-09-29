package com.czx.shiro_demo.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.czx.shiro_demo.tools.ObjectIdSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 角色
 */
@Data
@Document("role")
public class RoleEntity {

    @JSONField(serializeUsing = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;

    @JSONField(serializeUsing = ObjectIdSerializer.class)
    private List<ObjectId> permissionList;

}
