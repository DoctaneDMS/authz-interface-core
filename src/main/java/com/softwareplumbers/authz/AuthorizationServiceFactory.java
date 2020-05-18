/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.authz;

import com.softwareplumbers.common.immutablelist.AbstractImmutableList;

/**
 *
 * @author jonathan
 */
public interface AuthorizationServiceFactory<T extends Enum<T>, R extends Enum<R>, P extends AbstractImmutableList<?,P>> {
    AuthorizationService<T,R,P> getService(String name);
}
