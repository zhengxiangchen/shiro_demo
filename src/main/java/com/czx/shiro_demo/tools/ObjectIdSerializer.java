package com.czx.shiro_demo.tools;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 用于objectId的序列化
 */
public class ObjectIdSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object instanceof ObjectId) {
            ObjectId objectId = (ObjectId) object;
            out.writeString(objectId.toString());
            return;
        }
    }
}
