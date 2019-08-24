package com.brt.duet.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description duet密码加密
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param authcToken
	 * @param info
	 * @return 验证是否通过
	 * @description 密码验证
	 * @see org.apache.shiro.authc.credential.SimpleCredentialsMatcher#doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationInfo) 
	 */
	@Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
 
        Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));
        Object accountCredentials = getCredentials(info);
        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        return equals(tokenCredentials, accountCredentials);
    }
	
    /**
     * @author 方杰
     * @date 2019年7月22日
     * @param data 未加密的密码
     * @return 加密处理过的密码
     * @description 将传进来密码加密方法
     */
    private String encrypt(String data) {
        String encodePwd = data;//这里可以选择自己的密码验证方式 比如 md5或者sha256等
        return encodePwd;
    }

}
