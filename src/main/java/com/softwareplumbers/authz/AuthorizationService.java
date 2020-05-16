/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.authz;

import com.softwareplumbers.authz.AuthzExceptions.InvalidPath;
import com.softwareplumbers.authz.impl.PublicAuthorizationService;
import com.softwareplumbers.common.abstractquery.Query;
import com.softwareplumbers.common.immutablelist.AbstractImmutableList;
import javax.json.JsonObject;

/** Authorization service.
 * 
 * Adds application-specific authorizations to a Doctane repository. 
 * 
 * @author Jonathan Essex
 * @param <Type>
 * @param <Role> Range of roles on which we can perform authorization
 * @param <Path> Path-type object parameter
 */
public interface AuthorizationService<Type extends Enum<Type>, Role extends Enum<Role>, Path extends AbstractImmutableList<?, Path>> {
    
    /** Get the Access Control List for a Repository Object (Workspace or Document).
     * 
     * If getObjectACL(rootId, path).contains(Value.from(userMetadata)) returns 
     * true, the user has can perform the given role on the referenced repository object.
     * 
     * For the 'CREATE' role, path may not reference a valid object, we are asking for ACL
     * controlling creation of an object at the specified path.
     * 
     * For CREATE role, all parameters are mandatory
     * For READ, UPDATE, DELETE roles, only path is mandatory. However if data needed
     * for the role check is absent, this may generate an additional request to
     * the back-end repository.
     * 
     * @param path path from root to object
     * @param metadata metadata of object
     * @param role to get ACL for
     * @return An access control list that can be used to determine if a user has the given role for the object
     * @throws InvalidPath
     */
    Query getObjectACL(Path path, Type objectType, JsonObject metadata, Role role) throws InvalidPath;
    

    /** Get An Access Constraint for the given user searching on the given path.
     * 
     * Allows search operations to be constrained based on a user's permissions.
     * 
     * getAccessConstraint(userMetadata, rootId, pathTemplate).contains(Value.from(repositoryObject)) will return
     * false for any repositoryObject on the paths specified by (rootId, pathTemplate) if the specified user
     * does not have permission to view that repository object.
     * 
     * @param userMetadata User metadata for user performing the search
     * @param rootId Origin of search path
     * @param pathTemplate Path to search on (may contain wildcards).
     * @return An access constraint which can filter out search results for which the user has no permission to view
     */
    Query getAccessConstraint(JsonObject userMetadata, Path pathTemplate);    
    
    
    /** Get metadata for a given user Id.
     * 
     * Metadata may represent application specific permissions in a way entirely defined by the application; the
     * Json format is simply convenient as as generic way to structure data suitable for transfer over the wire.
     * 
     * @param userId
     * @return application specific metadata for that user Id 
     */
    JsonObject getUserMetadata(String userId);
    
    /** Get a public authorization service.
     *
     * @param <T> Type of objects handled
     * @param <R> Roles handled
     * @param <P> Path type
     * @return A public authorization service
     */
    public static <T extends Enum<T>, R extends Enum<R>, P extends AbstractImmutableList<?,P>> AuthorizationService<T,R,P> publicAuthz() {
        return new PublicAuthorizationService<>();
    }
}
