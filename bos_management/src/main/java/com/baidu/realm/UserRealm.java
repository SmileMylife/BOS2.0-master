package com.baidu.realm;

import com.baidu.service.base.RoleService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ZhangPei on 2017/8/13.
 */
@Service("userRealm")
public class UserRealm extends AuthorizingRealm {
    /*@Autowired
    private UserService us;*/
    @Autowired
    private RoleService rs;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
       /* //用于进行授权，要查找相应的权限需要经过角色表。principal主要的，资本的
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        //用当前用户去查角色表
        List<Role> list = rs.findByUser(user);
        for (int i = 0; i < list.size(); i++) {
            simpleAuthorizationInfo.addRole(list.get(i).getKeyword());
            Set<Permission> permissions = list.get(i).getPermissions();
            for (Permission permission : permissions) {
                simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
            }
        }
        return simpleAuthorizationInfo;*/
       return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
       /* //用于进行角色校验
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //前面进行了非空检验，这里不用了
        String username = usernamePasswordToken.getUsername();
        if (username == null) {
            return null;
        }
        User user = us.findByUsername(username);
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());*/
       return null;
    }
}
