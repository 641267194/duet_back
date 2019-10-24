package com.brt.duet.config.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.brt.duet.config.shiro.token.PhoneCodeToken;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 手机验证码验证
 */
public class PhoneCodeMatcher extends SimpleCredentialsMatcher {
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param authcToken
	 * @param info
	 * @return 验证是否通过
	 * @description 手机验证码验证
	 * @see org.apache.shiro.authc.credential.SimpleCredentialsMatcher#doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationInfo) 
	 */
	@Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        PhoneCodeToken token = (PhoneCodeToken) authcToken;
        Object tokenCredentials = token.getCredentials();
        Object accountCredentials = getCredentials(info);
        
        return equals(tokenCredentials, accountCredentials);
    }
	
	@Override
	protected Object getCredentials(AuthenticationInfo info) {
		String phone = info.getPrincipals().getPrimaryPrincipal().toString();
		// TODO: 获取验证码
        return "123456";
    }
	
}
