/*
 * Copyright (c) 1997, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.security;

/**
 * Abstract class for representing access to a system resource.
 * All permissions have a name (whose interpretation depends on the subclass),
 * as well as abstract functions for defining the semantics of the
 * particular Permission subclass.
 *
 * <p>Most {@code Permission} objects also include an "actions" list that
 * tells the actions that are permitted for the object.  For example,
 * for a {@code java.io.FilePermission} object, the permission name is
 * the pathname of a file (or directory), and the actions list
 * (such as "read, write") specifies which actions are granted for the
 * specified file (or for files in the specified directory).
 * The actions list is optional for {@code Permission} objects, such as
 * {@code java.lang.RuntimePermission},
 * that don't need such a list; you either have the named permission (such
 * as "system.exit") or you don't.
 *
 * <p>An important method that must be implemented by each subclass is
 * the {@code implies} method to compare Permissions. Basically,
 * "permission p1 implies permission p2" means that
 * if one is granted permission p1, one is naturally granted permission p2.
 * Thus, this is not an equality test, but rather more of a
 * subset test.
 *
 * <P> {@code Permission} objects are similar to {@code String} objects
 * in that they are immutable once they have been created. Subclasses should not
 * provide methods that can change the state of a permission
 * once it has been created.
 *
 * @see Permissions
 * @see PermissionCollection
 *
 *
 * @author Marianne Mueller
 * @author Roland Schemers
 * @since 1.2
 */

public abstract class Permission implements Guard, java.io.Serializable {

    @java.io.Serial
    private static final long serialVersionUID = -5636570222231596674L;

    /**
     * @serial The permission name.
     */
    private final String name;

    /**
     * Constructs a permission with the specified name.
     *
     * @param name name of the {@code Permission} object being created.
     *
     */

    public Permission(String name) {
        this.name = name;
    }

    /**
     * Implements the guard interface for a permission.
     * Returns silently if access is granted. Otherwise, throws
     * a {@code SecurityException}.
     *
     * @apiNote This method originally threw a {@code SecurityException} if a
     *       security manager was enabled and the requested access, specified
     *       by this permission, was not permitted.
     *       {@linkplain SecurityManager The Security Manager} is no longer
     *       supported; thus, this method always throws a
     *       {@code SecurityException}.
     *
     * @param object the object being guarded (currently ignored).
     *
     * @throws SecurityException always
     *
     * @see Guard
     * @see GuardedObject
     *
     */
    @Override
    public void checkGuard(Object object) throws SecurityException {
        throw new SecurityException("checking permissions is not supported");
    }

    /**
     * Checks if the specified permission's actions are "implied by"
     * this object's actions.
     * <P>
     * This must be implemented by subclasses of {@code Permission}, as they
     * are the only ones that can impose semantics on a {@code Permission}
     * object.
     *
     * @param permission the permission to check against.
     *
     * @return {@code true} if the specified permission is implied by this
     * object, {@code false} if not.
     */

    public abstract boolean implies(Permission permission);

    /**
     * Checks two {@code Permission} objects for equality.
     * <P>
     * Do not use the {@code equals} method for making access control
     * decisions; use the {@code implies} method.
     *
     * @param obj the object we are testing for equality with this object.
     *
     * @return {@code true} if both {@code Permission} objects are equivalent.
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns the hash code value for this {@code Permission} object.
     * <P>
     * The required {@code hashCode} behavior for {@code Permission} Objects is
     * the following:
     * <ul>
     * <li>Whenever it is invoked on the same {@code Permission} object more
     *     than once during an execution of a Java application, the
     *     {@code hashCode} method
     *     must consistently return the same integer. This integer need not
     *     remain consistent from one execution of an application to another
     *     execution of the same application.
     * <li>If two {@code Permission} objects are equal according to the
     *     {@code equals}
     *     method, then calling the {@code hashCode} method on each of the
     *     two {@code Permission} objects must produce the same integer result.
     * </ul>
     *
     * @return a hash code value for this object.
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns the name of this {@code Permission}.
     * For example, in the case of a {@code java.io.FilePermission},
     * the name will be a pathname.
     *
     * @return the name of this {@code Permission}.
     *
     */

    public final String getName() {
        return name;
    }

    /**
     * Returns the actions as a {@code String}. This is abstract
     * so subclasses can defer creating a {@code String} representation until
     * one is needed. Subclasses should always return actions in what they
     * consider to be their
     * canonical form. For example, two FilePermission objects created via
     * the following:
     *
     * <pre>
     *   perm1 = new FilePermission(p1,"read,write");
     *   perm2 = new FilePermission(p2,"write,read");
     * </pre>
     *
     * both return
     * "read,write" when the {@code getActions} method is invoked.
     *
     * @return the actions of this {@code Permission}.
     *
     */

    public abstract String getActions();

    /**
     * Returns an empty {@code PermissionCollection} for a given
     * {@code Permission} object, or {@code null} if
     * one is not defined. Subclasses of class {@code Permission} should
     * override this if they need to store their permissions in a particular
     * {@code PermissionCollection} object in order to provide the correct
     * semantics when the {@code PermissionCollection.implies} method is called.
     * If {@code null} is returned,
     * then the caller of this method is free to store permissions of this
     * type in any {@code PermissionCollection} they choose (one that uses
     * a Hashtable, one that uses a Vector, etc.).
     *
     * @return a new {@code PermissionCollection} object for this type of
     * {@code Permission}, or {@code null} if one is not defined.
     */

    public PermissionCollection newPermissionCollection() {
        return null;
    }

    /**
     * Returns a string describing this {@code Permission}.  The convention
     * is to specify the class name, the permission name, and the actions in
     * the following format: '("ClassName" "name" "actions")', or
     * '("ClassName" "name")' if actions list is {@code null} or empty.
     *
     * @return information about this {@code Permission}.
     */
    public String toString() {
        String actions = getActions();
        if (actions == null || actions.isEmpty()) { // OPTIONAL
            return "(\"" + getClass().getName() + "\" \"" + name + "\")";
        } else {
            return "(\"" + getClass().getName() + "\" \"" + name +
                 "\" \"" + actions + "\")";
        }
    }
}
