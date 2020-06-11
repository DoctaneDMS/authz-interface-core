# Core Authorization Service

This project contains core interfaces and classes which abstract common interactions
with an authorization system in a manner which is readily cacheable.

## Package com.softwareplumbers.authz

This package contains interface classes are used both client and server-side in order to
abstract operations on authorization system. 

## Package com.softwareplumbers.authz.impl

This package contains a generic implementation the authorization service APIs. In particlar:

* PublicAuthorizationService is a dummy service which always permits access to any resource
* LocalAuthoriztionService permits access to any resource if the user in in a locally defined list
* FederatedAuthorizationService permits access a resource if permitted by one of a set of underlying
  authorization services.

## Overview

Authorization to access a resource(e.g. a document, workspace or feed) is a function of user metadata and an ACL. This service 
allows an API tenant to retrieve the ACL for a resource; this ACL takes the form of a list of filters. A user is granted access 
to a document if any of the returned filters for the document returns 'true' when applied to the user's metadata.

The function which returns the ACL takes a repository object's metadata as an argument; the results will be cached for a predetermined period. 
The cached ACL is always refreshed if it does not grant access to a document for a user; thus, a change to resource metadata
which permits access to a data will always take effect immediately, wheres a change to resource metadata which removes access will only 
take effect when the cache entry expires.

A User's metadata is a union of data returned by the IDP and data returned by the authorization service's getUserMetadata method. The
data returned by the IDP are stored in the authentication token and thus cached at the client for the duration of the user session, 
and the data from the authorization service is cached by the Doctane server for a predetermined period.

For search operations the process is slightly different. The authorization service getAccessConstraint method is passed both the user
metadata and the path to be searched; the filters returned are passed in to the API search operation to ensure that only accessible
sub-resources are returned.

### Core authorization services

The following bean defines a public authorization service which grants access to all authenticated users.

```xml
    <bean id="authz.public" class="com.softwareplumbers.dms.rest.server.model.PublicAuthorizationService" scope="singleton"/>
```

The following bean defines a local authorization service which grants access to the named users (...in this case the user
name is DEFAULT_SERVICE_ACCOUNT) and will supply the given value (formatted as JSON) as user metadata.

```xml
    <bean name="authz.local" class="com.softwareplumbers.dms.rest.server.model.LocalAuthorizationService" scope="singleton">
        <property name="localUsers">
            <util:map>
                <entry key="DEFAULT_SERVICE_ACCOUNT" value='{ "serviceAccount" : true }'/>
            </util:map>
        </property>
    </bean>
```        

The following bean defines a federated authorization services created from two underlying authorization services authz.local
and authz.ti.

```xml
    <bean name="authz.federated" class="com.softwareplumbers.dms.rest.server.model.FederatedAuthorizationService" scope="singleton">
        <property name="authorizationServices" >
            <util:list value-type="com.softwareplumbers.dms.rest.server.model.AuthorizationService" >
                <ref bean="authz.ti"/>
                <ref bean="authz.local"/>
            </util:list>
        </property>
    </bean>
```

