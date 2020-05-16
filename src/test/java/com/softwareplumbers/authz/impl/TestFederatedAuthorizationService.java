package com.softwareplumbers.authz.impl;

import com.softwareplumbers.authz.AuthzExceptions.InvalidPath;
import com.softwareplumbers.common.immutablelist.QualifiedName;
import com.softwareplumbers.common.abstractquery.Query;
import java.math.BigDecimal;

import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jonathan
 */
public class TestFederatedAuthorizationService {
    
    enum AuthRoles { A, B };
    enum ObjectTypes { X, Y };
    
    private final JsonObject USER_METADATA_SERVICE_ACCOUNT = Json.createObjectBuilder()
        .add("serviceAccount", true)
        .build();

    private final JsonObject USER_METADATA_KBSL_BRANCH = Json.createObjectBuilder()
        .add("transactionBranches", Json.createArrayBuilder().add("KBSL"))
        .build();

    private final JsonObject USER_METADATA_MBUK_AND_SVC_AC = Json.createObjectBuilder()
        .add("transactionBranches", Json.createArrayBuilder().add("MBUK"))
        .add("serviceAccount", true)
        .build();

    private final JsonObject USER_METADATA_KBSL_AND_SVC_AC = Json.createObjectBuilder()
        .add("transactionBranches", Json.createArrayBuilder().add("KBSL"))
        .add("serviceAccount", true)
        .build();

    FederatedAuthorizationService<ObjectTypes,AuthRoles,QualifiedName> service = new FederatedAuthorizationService() {{
        addAuthorizationService(new LocalAuthorizationService() {{
            addLocalUser("userOne", USER_METADATA_SERVICE_ACCOUNT); 
            addLocalUser("userTwo", USER_METADATA_MBUK_AND_SVC_AC);
        }});
        addAuthorizationService(new LocalAuthorizationService() {{
            addLocalUser("userTwo", USER_METADATA_KBSL_BRANCH);
            addLocalUser("userThree", USER_METADATA_KBSL_BRANCH);
        }});
    }};

	@Test public void testGetUserMetadata() {
        assertThat(service.getUserMetadata("userOne"), equalTo(USER_METADATA_SERVICE_ACCOUNT));
        assertThat(service.getUserMetadata("userTwo"), equalTo(USER_METADATA_KBSL_AND_SVC_AC));
        assertThat(service.getUserMetadata("userThree"), equalTo(USER_METADATA_KBSL_BRANCH));
    }

    @Test public void testGetObjectACL() throws InvalidPath {
        Query acl = service.getObjectACL(QualifiedName.ROOT, null, null, AuthRoles.A);
        assertThat(acl.containsItem(USER_METADATA_SERVICE_ACCOUNT), equalTo(true));
        assertThat(acl.containsItem(USER_METADATA_KBSL_AND_SVC_AC), equalTo(true));
        assertThat(acl.containsItem(USER_METADATA_KBSL_BRANCH), equalTo(false));
    }    
}
