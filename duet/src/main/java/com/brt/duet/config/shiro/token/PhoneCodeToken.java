package com.brt.duet.config.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class PhoneCodeToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

	/**
	 * @description 
	 */
	private static final long serialVersionUID = 3546754450758804496L;

	/**
     * @description phone
     */
    private String phone;

    /**
     * @description code
     */
    private String code;

    /**
     * @description Whether or not 'rememberMe' should be enabled for the corresponding login attempt;
     *              default is <code>false</code>
     */
    private boolean rememberMe = false;

    /**
     * The location from where the login attempt occurs, or <code>null</code> if not known or explicitly
     * omitted.
     */
    /**
     * @description The location from where the login attempt occurs, or <code>null</code> if not known or explicitly
     *              omitted.
     */
    private String host;

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    public PhoneCodeToken() {
    }

    public PhoneCodeToken(final String phone, final String code) {
        this(phone, code, false, null);
    }

    public PhoneCodeToken(final String phone, final String code, final String host) {
        this(phone, code, false, host);
    }

    public PhoneCodeToken(final String phone, final String code, final boolean rememberMe) {
        this(phone, code, rememberMe, null);
    }

    public PhoneCodeToken(final String phone, final String code,
                                 final boolean rememberMe, final String host) {
        this.phone = phone;
        this.code = code;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getPrincipal() {
        return getPhone();
    }

    public Object getCredentials() {
        return getCode();
    }

    /**
     * Returns the host name or IP string from where the authentication attempt occurs.  May be <tt>null</tt> if the
     * host name/IP is unknown or explicitly omitted.  It is up to the Authenticator implementation processing this
     * token if an authentication attempt without a host is valid or not.
     * <p/>
     * <p>(Shiro's default Authenticator allows <tt>null</tt> hosts to support localhost and proxy server environments).</p>
     *
     * @return the host from where the authentication attempt occurs, or <tt>null</tt> if it is unknown or
     *         explicitly omitted.
     * @since 1.0
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host name or IP string from where the authentication attempt occurs.  It is up to the Authenticator
     * implementation processing this token if an authentication attempt without a host is valid or not.
     * <p/>
     * <p>(Shiro's default Authenticator
     * allows <tt>null</tt> hosts to allow localhost and proxy server environments).</p>
     *
     * @param host the host name or IP string from where the attempt is occurring
     * @since 1.0
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Returns <tt>true</tt> if the submitting user wishes their identity (principal(s)) to be remembered
     * across sessions, <tt>false</tt> otherwise.  Unless overridden, this value is <tt>false</tt> by default.
     *
     * @return <tt>true</tt> if the submitting user wishes their identity (principal(s)) to be remembered
     *         across sessions, <tt>false</tt> otherwise (<tt>false</tt> by default).
     * @since 0.9
     */
    public boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Sets if the submitting user wishes their identity (principal(s)) to be remembered across sessions.  Unless
     * overridden, the default value is <tt>false</tt>, indicating <em>not</em> to be remembered across sessions.
     *
     * @param rememberMe value indicating if the user wishes their identity (principal(s)) to be remembered across
     *                   sessions.
     * @since 0.9
     */
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    /*--------------------------------------------
    |               M E T H O D S               |
    ============================================*/

    /**
     * Clears out (nulls) the username, password, rememberMe, and inetAddress.  The password bytes are explicitly set to
     * <tt>0x00</tt> before nulling to eliminate the possibility of memory access at a later time.
     */
    public void clear() {
        this.phone = null;
        this.code = null;
        this.host = null;
        this.rememberMe = false;
    }

    /**
     * Returns the String representation.  It does not include the password in the resulting
     * string for security reasons to prevent accidentally printing out a password
     * that might be widely viewable).
     *
     * @return the String representation of the <tt>UsernamePasswordToken</tt>, omitting
     *         the password.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" - ");
        sb.append(phone);
        sb.append(", rememberMe=").append(rememberMe);
        if (host != null) {
            sb.append(" (").append(host).append(")");
        }
        return sb.toString();
    }

}