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


