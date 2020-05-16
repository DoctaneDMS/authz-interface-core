/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.authz.impl;

import com.softwareplumbers.authz.AuthorizationService;
import com.softwareplumbers.common.abstractquery.Query;
import com.softwareplumbers.common.abstractquery.Range;
import com.softwareplumbers.common.immutablelist.AbstractImmutableList;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;


/**
 *
 * @author SWPNET\jonessex
 * @param <Role>
 * @param <Path>
 */
public class LocalAuthorizationService<Type extends Enum<Type>, Role extends Enum<Role>, Path extends AbstractImmutableList<?,Path>> implements AuthorizationService<Type,Role,Path> {
    
    private Map<String, JsonObject> localUsers = new HashMap<>();
    
    public void setLocalUsers(Map<String,String> localUsers) {
        for (Map.Entry<String,String> entry : localUsers.entrySet()) {
            JsonReader reader = Json.createReader(new StringReader(entry.getValue()));
            this.localUsers.put(entry.getKey(), reader.readObject());
        }
    }
    
    public void addLocalUser(String username, JsonObject userMetadata) {
        localUsers.put(username, userMetadata);
    }

    @Override
    public Query getObjectACL(Path path, Type objectType, JsonObject metadata, Role role) {
        return Query.from("serviceAccount", Range.equals(JsonValue.TRUE));
    }

    @Override
    public Query getAccessConstraint(JsonObject userMetadata, Path pathTemplate) {
        if (userMetadata.getBoolean("serviceAccount", false))
            return Query.UNBOUNDED;
        else 
            return Query.EMPTY;
    }

    @Override
    public JsonObject getUserMetadata(String userId) {
        return localUsers.get(userId);
    }
}
