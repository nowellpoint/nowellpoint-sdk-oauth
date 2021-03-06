/*
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/

TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

1. Definitions.

"License" shall mean the terms and conditions for use, reproduction, and
distribution as defined by Sections 1 through 9 of this document.

"Licensor" shall mean the copyright owner or entity authorized by the copyright
owner that is granting the License.

"Legal Entity" shall mean the union of the acting entity and all other entities
that control, are controlled by, or are under common control with that entity.
For the purposes of this definition, "control" means (i) the power, direct or
indirect, to cause the direction or management of such entity, whether by
contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the
outstanding shares, or (iii) beneficial ownership of such entity.

"You" (or "Your") shall mean an individual or Legal Entity exercising
permissions granted by this License.

"Source" form shall mean the preferred form for making modifications, including
but not limited to software source code, documentation source, and configuration
files.

"Object" form shall mean any form resulting from mechanical transformation or
translation of a Source form, including but not limited to compiled object code,
generated documentation, and conversions to other media types.

"Work" shall mean the work of authorship, whether in Source or Object form, made
available under the License, as indicated by a copyright notice that is included
in or attached to the work (an example is provided in the Appendix below).

"Derivative Works" shall mean any work, whether in Source or Object form, that
is based on (or derived from) the Work and for which the editorial revisions,
annotations, elaborations, or other modifications represent, as a whole, an
original work of authorship. For the purposes of this License, Derivative Works
shall not include works that remain separable from, or merely link (or bind by
name) to the interfaces of, the Work and Derivative Works thereof.

"Contribution" shall mean any work of authorship, including the original version
of the Work and any modifications or additions to that Work or Derivative Works
thereof, that is intentionally submitted to Licensor for inclusion in the Work
by the copyright owner or by an individual or Legal Entity authorized to submit
on behalf of the copyright owner. For the purposes of this definition,
"submitted" means any form of electronic, verbal, or written communication sent
to the Licensor or its representatives, including but not limited to
communication on electronic mailing lists, source code control systems, and
issue tracking systems that are managed by, or on behalf of, the Licensor for
the purpose of discussing and improving the Work, but excluding communication
that is conspicuously marked or otherwise designated in writing by the copyright
owner as "Not a Contribution."

"Contributor" shall mean Licensor and any individual or Legal Entity on behalf
of whom a Contribution has been received by Licensor and subsequently
incorporated within the Work.

2. Grant of Copyright License.

Subject to the terms and conditions of this License, each Contributor hereby
grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free,
irrevocable copyright license to reproduce, prepare Derivative Works of,
publicly display, publicly perform, sublicense, and distribute the Work and such
Derivative Works in Source or Object form.

3. Grant of Patent License.

Subject to the terms and conditions of this License, each Contributor hereby
grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free,
irrevocable (except as stated in this section) patent license to make, have
made, use, offer to sell, sell, import, and otherwise transfer the Work, where
such license applies only to those patent claims licensable by such Contributor
that are necessarily infringed by their Contribution(s) alone or by combination
of their Contribution(s) with the Work to which such Contribution(s) was
submitted. If You institute patent litigation against any entity (including a
cross-claim or counterclaim in a lawsuit) alleging that the Work or a
Contribution incorporated within the Work constitutes direct or contributory
patent infringement, then any patent licenses granted to You under this License
for that Work shall terminate as of the date such litigation is filed.

4. Redistribution.

You may reproduce and distribute copies of the Work or Derivative Works thereof
in any medium, with or without modifications, and in Source or Object form,
provided that You meet the following conditions:

You must give any other recipients of the Work or Derivative Works a copy of
this License; and
You must cause any modified files to carry prominent notices stating that You
changed the files; and
You must retain, in the Source form of any Derivative Works that You distribute,
all copyright, patent, trademark, and attribution notices from the Source form
of the Work, excluding those notices that do not pertain to any part of the
Derivative Works; and
If the Work includes a "NOTICE" text file as part of its distribution, then any
Derivative Works that You distribute must include a readable copy of the
attribution notices contained within such NOTICE file, excluding those notices
that do not pertain to any part of the Derivative Works, in at least one of the
following places: within a NOTICE text file distributed as part of the
Derivative Works; within the Source form or documentation, if provided along
with the Derivative Works; or, within a display generated by the Derivative
Works, if and wherever such third-party notices normally appear. The contents of
the NOTICE file are for informational purposes only and do not modify the
License. You may add Your own attribution notices within Derivative Works that
You distribute, alongside or as an addendum to the NOTICE text from the Work,
provided that such additional attribution notices cannot be construed as
modifying the License.
You may add Your own copyright statement to Your modifications and may provide
additional or different license terms and conditions for use, reproduction, or
distribution of Your modifications, or for any such Derivative Works as a whole,
provided Your use, reproduction, and distribution of the Work otherwise complies
with the conditions stated in this License.

5. Submission of Contributions.

Unless You explicitly state otherwise, any Contribution intentionally submitted
for inclusion in the Work by You to the Licensor shall be under the terms and
conditions of this License, without any additional terms or conditions.
Notwithstanding the above, nothing herein shall supersede or modify the terms of
any separate license agreement you may have executed with Licensor regarding
such Contributions.

6. Trademarks.

This License does not grant permission to use the trade names, trademarks,
service marks, or product names of the Licensor, except as required for
reasonable and customary use in describing the origin of the Work and
reproducing the content of the NOTICE file.

7. Disclaimer of Warranty.

Unless required by applicable law or agreed to in writing, Licensor provides the
Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
including, without limitation, any warranties or conditions of TITLE,
NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are
solely responsible for determining the appropriateness of using or
redistributing the Work and assume any risks associated with Your exercise of
permissions under this License.

8. Limitation of Liability.

In no event and under no legal theory, whether in tort (including negligence),
contract, or otherwise, unless required by applicable law (such as deliberate
and grossly negligent acts) or agreed to in writing, shall any Contributor be
liable to You for damages, including any direct, indirect, special, incidental,
or consequential damages of any character arising as a result of this License or
out of the use or inability to use the Work (including but not limited to
damages for loss of goodwill, work stoppage, computer failure or malfunction, or
any and all other commercial damages or losses), even if such Contributor has
been advised of the possibility of such damages.

9. Accepting Warranty or Additional Liability.

While redistributing the Work or Derivative Works thereof, You may choose to
offer, and charge a fee for, acceptance of support, warranty, indemnity, or
other liability obligations and/or rights consistent with this License. However,
in accepting such obligations, You may act only on Your own behalf and on Your
sole responsibility, not on behalf of any other Contributor, and only if You
agree to indemnify, defend, and hold each Contributor harmless for any liability
incurred by, or claims asserted against, such Contributor by reason of your
accepting any such warranty or additional liability.

END OF TERMS AND CONDITIONS
 */

package com.nowellpoint.oauth.provider;

import java.io.IOException;
import java.net.URLEncoder;

import com.nowellpoint.oauth.OAuthServiceProvider;
import com.nowellpoint.oauth.exception.OAuthException;
import com.nowellpoint.oauth.http.HttpException;
import com.nowellpoint.oauth.http.HttpRequest;
import com.nowellpoint.oauth.http.HttpRequestBuilder;
import com.nowellpoint.oauth.http.HttpResponse;
import com.nowellpoint.oauth.impl.OAuthConstants;
import com.nowellpoint.oauth.model.Identity;
import com.nowellpoint.oauth.model.OrganizationInfo;
import com.nowellpoint.oauth.model.Token;
import com.nowellpoint.oauth.model.UserInfo;
import com.nowellpoint.oauth.request.BasicTokenRequest;
import com.nowellpoint.oauth.request.IdentityRequest;
import com.nowellpoint.oauth.request.RefreshTokenRequest;
import com.nowellpoint.oauth.request.RevokeTokenRequest;
import com.nowellpoint.oauth.request.VerifyTokenRequest;

public abstract class AbstractSalesforceProvider extends OAuthServiceProvider {

	/**
	 * 
	 */

	private static final long serialVersionUID = -6252791802295055242L;

	/**
	 * 
	 */

	private static final String API_VERSION = "35.0";

	/**
	 * 
	 */

	private static final String USER_FIELDS = "Id,Username,LastName,FirstName,Name,CompanyName,Division,Department,"
			+ "Title,Street,City,State,PostalCode,Country,Latitude,Longitude,"
			+ "Email,SenderEmail,SenderName,Signature,Phone,Fax,MobilePhone,Alias,"
			+ "CommunityNickname,IsActive,TimeZoneSidKey,LocaleSidKey,EmailEncodingKey,"
			+ "UserType,LanguageLocaleKey,EmployeeNumber,DelegatedApproverId,ManagerId,AboutMe";

	/**
	 * 
	 */

	private static final String ORGANIZATION_FIELDS = "Id,Name,InstanceName,Division,Street,City,State,PostalCode,Country,"
			+ "PrimaryContact,DefaultLocaleSidKey,LanguageLocaleKey,FiscalYearStartMonth";

	/**
	 * 
	 */

	public AbstractSalesforceProvider() {

	}
	
	/**
	 * 
	 */

	@Override
	public Token requestToken(BasicTokenRequest basicTokenRequest) throws OAuthException {
		
		HttpRequest request = null;
		try {
			request = new HttpRequestBuilder().target(getTokenEndpoint())
					.queryParameter(OAuthConstants.GRANT_TYPE_PARAMETER, OAuthConstants.PASSWORD_GRANT_TYPE)
					.queryParameter(OAuthConstants.CLIENT_ID_PARAMETER, basicTokenRequest.getClientId())
					.queryParameter(OAuthConstants.CLIENT_SECRET_PARAMETER, basicTokenRequest.getClientSecret())
					.queryParameter(OAuthConstants.USERNAME_PARAMETER, basicTokenRequest.getUsername())
					.queryParameter(OAuthConstants.PASSWORD_PARAMETER, URLEncoder.encode(basicTokenRequest.getPassword(), "UTF-8"))
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		} 
		
		return requestToken(request);
	}

	@Override
	public Token requestToken(VerifyTokenRequest verifyTokenRequest) throws OAuthException {
		
		HttpRequest request;
		try {
			request = new HttpRequestBuilder().target(getTokenEndpoint())
					.queryParameter(OAuthConstants.GRANT_TYPE_PARAMETER, OAuthConstants.AUTHORIZATION_GRANT_TYPE)
					.queryParameter(OAuthConstants.CLIENT_ID_PARAMETER, verifyTokenRequest.getClientId())
					.queryParameter(OAuthConstants.CLIENT_SECRET_PARAMETER, verifyTokenRequest.getClientSecret())
					.queryParameter(OAuthConstants.REDIRECT_URI_PARAMETER, verifyTokenRequest.getCallbackUrl())
					.queryParameter(OAuthConstants.CODE_PARAMETER, verifyTokenRequest.getCode())
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		}
		
		return requestToken(request);
	}

	@Override
	public Identity getIdentity(IdentityRequest identityRequest) throws OAuthException {
		
		HttpRequest request;
		try {
			request = new HttpRequestBuilder().target(identityRequest.getIdentityUrl())
					.queryParameter(OAuthConstants.OAUTH_TOKEN_PARAMETER, identityRequest.getAccessToken())
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		}
		
		return requestIdentity(request);
	}

	@Override
	public void revokeToken(RevokeTokenRequest revokeTokenRequest) throws OAuthException {
		
		HttpRequest request;
		try {
			request = new HttpRequestBuilder().target(getRevokeEndpoint())
					.queryParameter(OAuthConstants.TOKEN_PARAMETER, revokeTokenRequest.getAccessToken())
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		}
		
		try {
			request.post();
		} catch (HttpException e) {
			throw new OAuthException(e.getMessage());
		} finally {
			request.clear();
		}
	}

	@Override
	public Token refreshToken(RefreshTokenRequest refreshTokenRequest) throws OAuthException {
		
		HttpRequest request;
		try {
			request = new HttpRequestBuilder().target(getTokenEndpoint())
					.header(OAuthConstants.GRANT_TYPE_PARAMETER, OAuthConstants.REFRESH_GRANT_TYPE)
					.header(OAuthConstants.CLIENT_ID_PARAMETER, refreshTokenRequest.getClientId())
					.header(OAuthConstants.CLIENT_SECRET_PARAMETER, refreshTokenRequest.getClientSecret())
					.header(OAuthConstants.REFRESH_GRANT_TYPE, refreshTokenRequest.getRefreshToken())
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		}
		
		return requestToken(request);
	}

	public UserInfo getUserInfo(Token token, Identity identity) throws OAuthException {
		
		String url = getUserInfoUrl(identity);
		
		HttpRequest request;
		try {
			request = new HttpRequestBuilder().target(url)
					.queryParameter("fields", USER_FIELDS)
					.bearerToken(token.getAccessToken())
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		}
		
		return requestUserInfo(request);
	}

	public OrganizationInfo getOrganizationInfo(Token token, Identity identity) throws OAuthException {
		
		String url = getOrganizationInfoUrl(identity);
		
		HttpRequest request;
		try {
			request = new HttpRequestBuilder().target(url)
					.queryParameter("fields", ORGANIZATION_FIELDS)
					.bearerToken(token.getAccessToken())
					.build();
		} catch (IOException e) {
			throw new OAuthException(e.getMessage());
		}
		
		return requestOrganizationInfo(request);
	}

	public abstract String getTokenEndpoint();

	public abstract String getRevokeEndpoint();
	
	/**
	 * getUserInfoUrl
	 * @param identity
	 * @return URL to obtain UserInfo
	 */
	
	private String getUserInfoUrl(Identity identity) {
		return new StringBuilder().append(getSObjectUrl(identity))
				.append("User/")
				.append(identity.getUserId())
				.toString();
	}
	
	/**
	 * getOrganizationInfoUrl
	 * @param identity
	 * @return URL to obtain OrganizationInfo
	 */
	
	private String getOrganizationInfoUrl(Identity identity) {
		return new StringBuilder().append(getSObjectUrl(identity))
				.append("Organization/")
				.append(identity.getOrganizationId())
				.toString();
	}

	/**
	 * getSObjectUrl
	 * @param identity
	 * @return the SObject URL
	 */
	
	private String getSObjectUrl(Identity identity) {
		return identity.getUrls().getSObjects().replace("{version}", API_VERSION);
	}
	
	/**
	 * requestToken
	 * @param request
	 * @return the requested Token
	 * @throws OAuthException
	 */
	
	private Token requestToken(HttpRequest request) throws OAuthException {
		HttpResponse<Token> response = null;
		try {
			response = request.post(Token.class);
		} catch (HttpException e) {
			throw new OAuthException(e.getMessage());
		} finally {
			request.clear();
		}
		
		return response.getEntity();
	}
	
	/**
	 * requestIdentity
	 * @param request
	 * @return Identity details for the current user session
	 * @throws OAuthException
	 */
	
	private Identity requestIdentity(HttpRequest request) throws OAuthException {
		HttpResponse<Identity> response = null;
		try {
			response = request.post(Identity.class);
		} catch (HttpException e) {
			throw new OAuthException(e.getMessage());
		} finally {
			request.clear();
		}
		
		return response.getEntity();
	}
	
	/**
	 * requestOrganizationInfo
	 * @param request
	 * @return Organization Information for the current user session
	 * @throws OAuthException
	 */
	
	private OrganizationInfo requestOrganizationInfo(HttpRequest request) throws OAuthException {
		HttpResponse<OrganizationInfo> response = null;
		try {
			response = request.get(OrganizationInfo.class);
		} catch (HttpException e) {
			throw new OAuthException(e.getMessage());
		} finally {
			request.clear();
		}
		
		return response.getEntity();
	}
	
	/**
	 * requestUserInfo
	 * @param request
	 * @return User Information for the current user session
	 * @throws OAuthException
	 */
	
	private UserInfo requestUserInfo(HttpRequest request) throws OAuthException {
		HttpResponse<UserInfo> response = null;
		try {
			response = request.get(UserInfo.class);
		} catch (HttpException e) {
			throw new OAuthException(e.getMessage());
		} finally {
			request.clear();
		}
		
		return response.getEntity();
	}
}