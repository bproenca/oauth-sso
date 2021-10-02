package br.com.bcp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String scope : getScopeAuthorities(jwt)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(DEFAULT_SCOPE_PREFIX + scope));
		}
        for (String role : getRoleAuthorities(jwt)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE_PREFIX + role));
		}
        return grantedAuthorities;
	}

    @SuppressWarnings("unchecked")
    private Collection<String> getRoleAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<String> roles = ((List<String>) realmAccess.get("roles"));
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles;
    }

    private Collection<String> getScopeAuthorities(Jwt jwt) {
		Object authorities = jwt.getClaim("scope");
		if (authorities instanceof String) {
			if (StringUtils.hasText((String) authorities)) {
				return Arrays.asList(((String) authorities).split(" "));
			}
			return Collections.emptyList();
		}
		if (authorities instanceof Collection) {
			return castAuthoritiesToCollection(authorities);
		}
		return Collections.emptyList();
	}

    @SuppressWarnings("unchecked")
	private Collection<String> castAuthoritiesToCollection(Object authorities) {
		return (Collection<String>) authorities;
	}
}