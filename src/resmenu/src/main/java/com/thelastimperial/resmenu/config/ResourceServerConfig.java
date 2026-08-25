package com.thelastimperial.resmenu.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class ResourceServerConfig {
    private final String keyId;
    private final String publicKey;
    private final String privateKey;

	public ResourceServerConfig(
        @Value("${com.thelastimperial.resmenu.security.keyid}")
        String keyId,
        @Value("${com.thelastimperial.resmenu.security.publicKey}")
        String publicKey,
        @Value("${com.thelastimperial.resmenu.security.privateKey}")
        String privateKey
    ) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.keyId = keyId;
	}

    @Bean
    public JWK getOAuth2RsaKey() throws Exception {

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory publicKf = KeyFactory.getInstance("RSA");

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory privateKf = KeyFactory.getInstance("RSA");

		return new RSAKey
                .Builder((RSAPublicKey)publicKf.generatePublic(publicSpec))
				.privateKey((RSAPrivateKey)privateKf.generatePrivate(privateSpec))
				.keyID(keyId)
				.build();
    }

	@Bean
	public JWKSource<SecurityContext> jwkSource(JWK jwk) {
		JWKSet jwkSet = new JWKSet(jwk);
		return new ImmutableJWKSet<>(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
