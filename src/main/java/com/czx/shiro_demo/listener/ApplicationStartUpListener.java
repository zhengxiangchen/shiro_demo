package com.czx.shiro_demo.listener;

import cn.hutool.core.util.IdUtil;
import com.czx.shiro_demo.entity.PermissionEntity;
import com.czx.shiro_demo.entity.RoleEntity;
import com.czx.shiro_demo.entity.UserEntity;
import com.czx.shiro_demo.repository.PermissionRepository;
import com.czx.shiro_demo.repository.RoleRepository;
import com.czx.shiro_demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ApplicationStartUpListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //初始化权限
        initPermission();

        //初始化角色
        initRole();

        //初始化用户
        initUser();
    }



    /**
     * 初始化权限
     */
    private void initPermission() {
        List<PermissionEntity> list = permissionRepository.findAll();
        if(list != null && list.size() > 0){
            log.info("权限已存在----------->不需要初始化");
        }else{
            log.info("初始化基础权限");
            list = new ArrayList<>();
            PermissionEntity permission_add = new PermissionEntity();
            permission_add.setName("新增权限");
            permission_add.setKey("permission:add");
            list.add(permission_add);

            PermissionEntity permission_delete = new PermissionEntity();
            permission_delete.setName("删除权限");
            permission_delete.setKey("permission:delete");
            list.add(permission_delete);

            PermissionEntity permission_update = new PermissionEntity();
            permission_update.setName("修改权限");
            permission_update.setKey("permission:update");
            list.add(permission_update);

            PermissionEntity permission_view = new PermissionEntity();
            permission_view.setName("查看权限");
            permission_view.setKey("permission:view");
            list.add(permission_view);


            PermissionEntity role_add = new PermissionEntity();
            role_add.setName("新增角色");
            role_add.setKey("role:add");
            list.add(role_add);

            PermissionEntity role_delete = new PermissionEntity();
            role_delete.setName("删除角色");
            role_delete.setKey("role:delete");
            list.add(role_delete);

            PermissionEntity role_update = new PermissionEntity();
            role_update.setName("修改角色");
            role_update.setKey("role:update");
            list.add(role_update);

            PermissionEntity role_view = new PermissionEntity();
            role_view.setName("查看角色");
            role_view.setKey("role:view");
            list.add(role_view);


            PermissionEntity user_add = new PermissionEntity();
            user_add.setName("新增用户");
            user_add.setKey("user:add");
            list.add(user_add);

            PermissionEntity user_delete = new PermissionEntity();
            user_delete.setName("删除用户");
            user_delete.setKey("user:delete");
            list.add(user_delete);

            PermissionEntity user_update = new PermissionEntity();
            user_update.setName("修改用户");
            user_update.setKey("user:update");
            list.add(user_update);

            PermissionEntity user_view = new PermissionEntity();
            user_view.setName("查看用户");
            user_view.setKey("user:view");
            list.add(user_view);

            permissionRepository.saveAll(list);
        }


    }


    /**
     * 初始化角色
     */
    private void initRole() {

        List<RoleEntity> roleEntityList = roleRepository.findAll();
        if(roleEntityList != null && roleEntityList.size() > 0){
            log.info("超级管理员角色已存在----------->不需要初始化");
        }else{
            log.info("初始化超级管理员角色");
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName("超级管理员");
            roleRepository.save(roleEntity);
        }
    }


    /**
     * 初始化管理员账号
     * @return
     */
    private void initUser() {
        List<UserEntity> userList = userRepository.findAll();
        if(userList.size() > 0){
            log.info("系统已存在账号, 不需要初始化超级管理员账号");
        }else{
            log.info("初始化超级管理员账号");
            RoleEntity roleEntity = roleRepository.findByName("超级管理员");

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("superadmin");
            String salt = IdUtil.objectId();
            userEntity.setSalt(salt);
            userEntity.setPassword(new Md5Hash("123456", salt, 2).toString());
            List<ObjectId> roleList = new ArrayList<>();
            roleList.add(roleEntity.getId());
            userEntity.setRoleList(roleList);

            userRepository.save(userEntity);
            log.info("初始化成功!");
        }
    }
}
