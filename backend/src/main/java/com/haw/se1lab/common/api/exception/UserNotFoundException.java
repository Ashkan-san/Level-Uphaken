package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.EmailType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Exception für nicht gefundene Benutzer
 *
 * @author Kjell May
 */
public class UserNotFoundException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String USER_WITH_EMAIL_NOT_FOUND_MESSAGE = "Konnte Benutzer mit Email %s nicht finden.";

    /* ---- Member Fields ---- */

    private final EmailType emailAddress;

    /* ---- Constructors ---- */

    public UserNotFoundException(EmailType emailAddress) {
        super(String.format(USER_WITH_EMAIL_NOT_FOUND_MESSAGE, emailAddress.getEmailAddress()));
        this.emailAddress = emailAddress;
    }

    /* ---- Getters/Setters ---- */

    public EmailType getEmail() {
        return emailAddress;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
