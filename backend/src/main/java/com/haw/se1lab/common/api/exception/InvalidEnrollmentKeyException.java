package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.Password;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class InvalidEnrollmentKeyException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String INVALID_ENROLLMENT_KEY = "Invalid Enrollment Key: %s";

    /* ---- Member Fields ---- */

    private Password enrollmentKey;

    /* ---- Constructors ---- */

    public InvalidEnrollmentKeyException(Password enrollmentKey) {
        super(String.format(INVALID_ENROLLMENT_KEY, enrollmentKey.toString()));
        this.enrollmentKey = enrollmentKey;
    }

    /* ---- Getters/Setters ---- */

    public Password getEnrollmentKey() {
        return enrollmentKey;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */


}