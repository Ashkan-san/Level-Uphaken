package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.EmailType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserAlreadyInSubjectException extends Exception {
    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String USER_WITH_EMAIL_ALREADY_EXISTS = "Benutzer mit EMail %s bereits in Liste enthalten.";

    /* ---- Member Fields ---- */

    private final EmailType emailAddress;

    /* ---- Constructors ---- */

    public UserAlreadyInSubjectException(EmailType emailAddress) {
        super(String.format(USER_WITH_EMAIL_ALREADY_EXISTS, emailAddress.getEmailAddress()));
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
