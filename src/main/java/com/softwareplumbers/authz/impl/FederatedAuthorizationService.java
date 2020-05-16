/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.authz.impl;

import com.softwareplumbers.authz.AuthorizationService;
import com.softwareplumbers.authz.AuthzExceptions.InvalidPath;
import com.softwareplumbers.common.abstractquery.Query;
import com.softwareplumbers.common.immutablelist.AbstractImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author SWPNET\jonessex
 */
public class FederatedAuthorizationService<Type extends Enum<Type>, Role extends Enum<Role>, Path extends AbstractImmutableList<?,Path>> implements AuthorizationService<Type,Role,Path> {
    
    private List<AuthorizationService<Type,Role,Path>> authorizationServices = new ArrayList<>();
    
    public void setAuthorizationServices(List<AuthorizationService<Type,Role,Path>> services) {
        this.authorizationServices = services;
    }
    
    public void addAuthorizationService(AuthorizationService<Type,Role,Path> service) {
        this.authorizationServices.add(service);
    }
    
    public FederatedAuthorizationService() { }
    

    @Override
    public Query getObjectACL(Path path, Type objectType, JsonObject metadata, Role role) throws InvalidPath {
        Query result = Query.EMPTY;
        for (AuthorizationService service : authorizationServices)
            result = result.union(service.getObjectACL(path, objectType, metadata, role));
        return result;
    }

    @Override
    public Query getAccessConstraint(JsonObject userMetadata, Path pathTemplate) {
        Query result = Query.EMPTY;
        for (AuthorizationService service : authorizationServices)
            result = result.union(service.getAccessConstraint(userMetadata, pathTemplate));
        return result;
    }

    @Override
    public JsonObject getUserMetadata(String userId) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (AuthorizationService service : authorizationServices) {
            JsonObject umd = service.getUserMetadata(userId);
            if (umd != null ) {
                for (Map.Entry<String,JsonValue> entry : umd.entrySet())
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }
    
}
