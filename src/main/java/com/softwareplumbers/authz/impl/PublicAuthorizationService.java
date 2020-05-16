/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.authz.impl;

import com.softwareplumbers.authz.AuthorizationService;
import com.softwareplumbers.common.abstractquery.Query;
import com.softwareplumbers.common.immutablelist.AbstractImmutableList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;


/**
 *
 * @author jonathan
 */
public class PublicAuthorizationService<Type extends Enum<Type>, Role extends Enum<Role>, Path extends AbstractImmutableList<?,Path>> implements AuthorizationService<Type,Role,Path> {
  
    public static final JsonObject EMPTY_METADATA = Json.createObjectBuilder().build();
    @Override
    public Query getObjectACL(Path path, Type objectType, JsonObject metadata, Role role) {
        return Query.UNBOUNDED;
    }

    @Override
    public Query getAccessConstraint(JsonObject userMetadata, Path pathTemplate) {
        return Query.UNBOUNDED;
    }

    @Override
    public JsonObject getUserMetadata(String userId) {
        return EMPTY_METADATA;
    }
}
