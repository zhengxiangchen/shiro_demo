package com.czx.shiro_demo.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.czx.shiro_demo.tools.ObjectIdSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 权限
 */
@Data
@Document("permission")
public class PermissionEntity {
    @JSONField(serializeUsing = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;

    private String key;

    private Boolean canDelete = false;
}
