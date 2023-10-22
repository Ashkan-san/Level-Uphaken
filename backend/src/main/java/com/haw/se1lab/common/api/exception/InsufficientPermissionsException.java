package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.RoleType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Exception für Zugriffe auf Methoden mit nicht zureichenden Rechten (durch Rolle)
 *
 * @author Kjell May
 */
public class InsufficientPermissionsException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String USER_ROLE_MESSAGE = "Benutzer hat Rolle %s und damit keine Berechtigung";

    /* ---- Member Fields ---- */
    private RoleType role;

    /* ---- Constructors ---- */

    public InsufficientPermissionsException(RoleType role) {
        super(String.format(USER_ROLE_MESSAGE, role));
        this.role = role;
    }

    /* ---- Getters/Setters ---- */

    public RoleType getRole() {
        return role;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
