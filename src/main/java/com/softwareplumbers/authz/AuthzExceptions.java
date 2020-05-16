/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.authz;

/**
 *
 * @author jonathan
 */
public class AuthzExceptions {
    
    /** Base exception.
     * 
     * All checked Feed exceptions will be a subclass.
     * 
     */
    public static class BaseException extends Exception {
        public BaseException(String reason) {
            super(reason);
        }
    }
    
    public static class BaseRuntimeException extends RuntimeException {
        public BaseRuntimeException(BaseException e) {
            super(e);
        }
        
    }
    
    /** Throw if a path is invalid
     * 
     */
    public static class InvalidPath extends BaseException {
        public final Object path;
        public InvalidPath(Object path) {
            super("Invalid Path: " + path.toString());
            this.path = path;
        }
    }
    
}
