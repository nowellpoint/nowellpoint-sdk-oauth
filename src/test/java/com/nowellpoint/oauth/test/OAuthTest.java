package com.nowellpoint.oauth.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.security.auth.login.LoginException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nowellpoint.oauth.OAuthConfig;
import com.nowellpoint.oauth.OAuthServiceProvider;
import com.nowellpoint.oauth.OAuthSession;
import com.nowellpoint.oauth.model.Identity;
import com.nowellpoint.oauth.model.OrganizationInfo;
import com.nowellpoint.oauth.model.Token;

public class OAuthTest {
	
	private static OAuthServiceProvider provider;
	private static OAuthSession session;
	
	@BeforeClass
	public static void init() {
		
		provider = new OAuthConfig().setClientId(System.getenv("CLIENT_ID"))
					.setClientSecret(System.getenv("CLIENT_SECRET"))
					.setUseSandbox(Boolean.FALSE)
					.build();
		
		session = new OAuthSession();
		session.setOAuthServiceProvider(provider);
		
		try {
			session.login(System.getenv("SALESFORCE_USERNAME"), System.getenv("SALESFORCE_PASSWORD"), System.getenv("SALESFORCE_SECURITY_TOKEN"));
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		assertNotNull(session.getSubject());
	}
	
	@AfterClass
	public static void cleanup() {
		try {
			session.logout();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		assertNull(session.getToken());
		assertNull(session.getIdentity());
	}

	@Test
	public void testSession() {		
		Token token = session.getToken();
		Identity identity = session.getIdentity();
		
		assertNotNull(token);
		assertNotNull(identity);
		
		System.out.println(token.getAccessToken());
		System.out.println(identity.getDisplayName());
	}
	
	@Test
	public void testUserInfo() {
		try {
			assertNotNull(session.getUserInfo());
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOrganizationInfo() {
		try {
			OrganizationInfo organizationInfo = session.getOrganizationInfo();
			assertNotNull(organizationInfo);
			System.out.println(organizationInfo.getName());
			System.out.println(organizationInfo.getAttributes().getUrl());
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
}