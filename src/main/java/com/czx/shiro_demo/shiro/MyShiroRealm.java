package com.czx.shiro_demo.shiro;

import com.czx.shiro_demo.entity.PermissionEntity;
import com.czx.shiro_demo.entity.RoleEntity;
import com.czx.shiro_demo.entity.UserEntity;
import com.czx.shiro_demo.repository.PermissionRepository;
import com.czx.shiro_demo.repository.RoleRepository;
import com.czx.shiro_demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;


/**
 * 自定义的登录验证及权限设置
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * 认证信息.(身份验证)----校验用户名密码
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("登录验证------>用户名 = [{}], 密码 = [{}]", token.getPrincipal(), token.getCredentials());
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        //根据用户名查询密码
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            log.info("用户不存在");
            return null;
        }
        String password = userEntity.getPassword();
        //加密盐---从用户表中取出
        String salt = userEntity.getSalt();

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userEntity,//正常存放整个user对象---这里存什么后面subject.get登录用户的时候得到的就是什么
                password,//数据库中的加密密码
                ByteSource.Util.bytes(salt),//加密盐--从user实体中获取--是在新增用户的时候存入user表中的
                getName()//固定写法--当前的realm
        );

        return authenticationInfo;
    }





    /**
     * 此方法调用hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*
         * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
         * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
         * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
         * 缓存过期之后会再次执行。
         */
        UserEntity user = (UserEntity) principals.getPrimaryPrincipal();
        log.info("[{}]权限配置", user.getUsername());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        if(user.getUsername().equals("superadmin")){
            authorizationInfo.addRole("超级管理员");
            authorizationInfo.addStringPermission("*:*");
            log.info("权限配置已完成-------->角色 = {}, 权限 = {}", authorizationInfo.getRoles(), authorizationInfo.getStringPermissions());
            return authorizationInfo;
        }

        //获取到角色id列表
        List<ObjectId> roleIdList = user.getRoleList();

        for(ObjectId roleId : roleIdList){
            Optional<RoleEntity> roleEntityOptional = roleRepository.findById(roleId);
            if(roleEntityOptional.isPresent()){
                RoleEntity roleEntity = roleEntityOptional.get();
                //设置角色
                authorizationInfo.addRole(roleEntity.getName());


                List<ObjectId> permissionIdList = roleEntity.getPermissionList();
                for(ObjectId permissionId : permissionIdList){
                    Optional<PermissionEntity> permissionEntityOptional = permissionRepository.findById(permissionId);
                    if(permissionEntityOptional.isPresent()){
                        PermissionEntity permissionEntity = permissionEntityOptional.get();
                        //设置权限
                        authorizationInfo.addStringPermission(permissionEntity.getKey());
                    }
                }

            }
        }
        log.info("权限配置已完成-------->{}", authorizationInfo.getStringPermissions());
        return authorizationInfo;
    }



    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
