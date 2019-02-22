package habuma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		   security.checkTokenAccess("isAuthenticated()");
	}
	
	// Configure the token store and authentication manager
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//@formatter:off
		endpoints
			.tokenStore(tokenStore())
			.authenticationManager(authenticationManager);
		//@formatter:on
	}

	// Configure a client store. In-memory for simplicity, but consider other
	// options for real apps.
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//@formatter:off
		clients
			.inMemory()
				.withClient("myclient")
				.secret("secret")
				.authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials")
				.scopes("read")
				.redirectUris("http://localhost:9191/x")
				.accessTokenValiditySeconds(86400); // 24 hours
		//@formatter:on
	}

	// A token store bean. In-memory token store
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

}
