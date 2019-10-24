package com.brt.duet.config.shiro;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.brt.duet.config.shiro.matcher.PhoneCodeMatcher;
import com.brt.duet.config.shiro.matcher.UsernamePasswordMatcher;
import com.brt.duet.config.shiro.realm.PhoneCodeRealm;
import com.brt.duet.config.shiro.realm.UsernamePasswordRealm;
import com.brt.duet.config.shiro.token.PhoneCodeToken;

/**
 * @author 方杰
 * @date 2019年07月22日
 * @description shiro配置类
 */
@Configuration
public class ShiroConfig {
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param securityManager
	 * @return 
	 * @description Shiro的Web过滤器Factory 命名:shiroFilter
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//Shiro的核心安全接口,这个属性是必须的
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		/*定义shiro过滤链  Map结构/
		 * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
		 * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
		 * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
		 */
		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
		filters.put("authc", new ShiroAuthFilter());//将自定义 的FormAuthenticationFilter注入shiroFilter中
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
         /* 过滤链定义，从上向下顺序执行，一般将 / ** 放在最为下边:这是一个坑呢，一不小心代码就不好使了;
          authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问 */
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/login/emp", "anon");
		filterChainDefinitionMap.put("/logout", "anon");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 
	 * @description securityManager,设置Realm为重写的UserRealm
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		Set<Realm> realms = new HashSet<>();
		realms.add(usernamePasswordRealm());
		realms.add(phoneCodeRealm());
		securityManager.setRealms(realms);
		return securityManager;
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 用户名密码验证
	 * @description Shiro Realm 继承自AuthorizingRealm的自定义Realm,即指定Shiro验证用户登录的类为自定义的
	 */
	@Bean
	public UsernamePasswordRealm usernamePasswordRealm() {
		UsernamePasswordRealm userRealm = new UsernamePasswordRealm();
		userRealm.setAuthenticationTokenClass(UsernamePasswordToken.class);
		userRealm.setCredentialsMatcher(new UsernamePasswordMatcher());
		return userRealm;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 手机验证码验证
	 * @description Shiro Realm 继承自AuthorizingRealm的自定义Realm,即指定Shiro验证用户登录的类为自定义的
	 */
	@Bean
	public PhoneCodeRealm phoneCodeRealm() {
		PhoneCodeRealm userRealm = new PhoneCodeRealm();
		userRealm.setAuthenticationTokenClass(PhoneCodeToken.class);
		userRealm.setCredentialsMatcher(new PhoneCodeMatcher());
		return userRealm;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 
	 * @description Shiro生命周期处理器
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 
	 * @description 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 *				 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 */
	@Bean
	@DependsOn({"lifecycleBeanPostProcessor"})
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
}
