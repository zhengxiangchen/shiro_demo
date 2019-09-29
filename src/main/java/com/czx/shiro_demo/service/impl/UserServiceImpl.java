package com.czx.shiro_demo.service.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.UserEntity;
import com.czx.shiro_demo.repository.UserRepository;
import com.czx.shiro_demo.service.IUserService;
import com.czx.shiro_demo.tools.ResponseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 新增用户接口
     * @param userEntity
     * @return
     */
    @Override
    public JSONObject add(UserEntity userEntity) {
        String username = userEntity.getUsername();
        UserEntity checkUsernameEntity = userRepository.findByUsername(username);
        if(checkUsernameEntity != null){
            return ResponseFormat.buildResponseMap("10001", new JSONObject());
        }
        String salt = IdUtil.objectId();
        userEntity.setSalt(salt);
        String password = new Md5Hash(userEntity.getPassword(), salt, 2).toString();
        userEntity.setPassword(password);

        userRepository.save(userEntity);

        log.info("新增成功--->{}", JSONObject.toJSONString(userEntity));
        return ResponseFormat.buildResponseMap("200", new JSONObject());
    }
}
