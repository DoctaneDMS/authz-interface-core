package com.softwareplumbers.authz.impl;

import com.softwareplumbers.common.immutablelist.QualifiedName;
import com.softwareplumbers.common.abstractquery.Query;

import javax.json.Json;
import javax.json.JsonObject;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestLocalAuthorizationService {
    
    private enum ServiceRoles { A, B };
    enum ObjectTypes { X, Y };
    
    private final JsonObject USER_METADATA_SERVICE_ACCOUNT = Json.createObjectBuilder()
        .add("serviceAccount", true)
        .build();
    
    LocalAuthorizationService<ObjectTypes,ServiceRoles,QualifiedName> service = new LocalAuthorizationService<ObjectTypes,ServiceRoles,QualifiedName>() {{
       addLocalUser("userOne", USER_METADATA_SERVICE_ACCOUNT); 
       addLocalUser("userTwo", PublicAuthorizationService.EMPTY_METADATA);
    }};

	@Test public void testGetUserMetadata() {
        assertThat(service.getUserMetadata("userOne"), equalTo(USER_METADATA_SERVICE_ACCOUNT));
        assertThat(service.getUserMetadata("userTwo"), equalTo(PublicAuthorizationService.EMPTY_METADATA));
    }
    
    @Test public void testGetObjectACL() {
        Query acl = service.getObjectACL(QualifiedName.ROOT, ObjectTypes.X, null, ServiceRoles.A);
        assertThat(acl.containsItem(USER_METADATA_SERVICE_ACCOUNT), equalTo(true));
        assertThat(acl.containsItem(PublicAuthorizationService.EMPTY_METADATA), equalTo(false));
    }    
}
