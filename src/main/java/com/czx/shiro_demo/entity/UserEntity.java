package com.czx.shiro_demo.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.czx.shiro_demo.tools.ObjectIdSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 用户
 */
@Data
@Document("user")
public class UserEntity {

    @JSONField(serializeUsing = ObjectIdSerializer.class)
    private ObjectId id;

    private String username;

    private String password;

    private String salt;

    @JSONField(serializeUsing = ObjectIdSerializer.class)
    private List<ObjectId> roleList;

}
