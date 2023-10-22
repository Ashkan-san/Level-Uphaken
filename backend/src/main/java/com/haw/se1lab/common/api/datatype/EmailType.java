package com.haw.se1lab.common.api.datatype;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Repräsentiert Email Datentyp.
 *
 * @author Isabell Schettler
 * @author Philip Gisella
 *
 */
@SuppressWarnings("JpaObjectClassSignatureInspection")
@Embeddable
public class EmailType implements Serializable {

    /* ---- Class Fields ---- */

    private static final Pattern EMAIL_TYPE_PATTERN = Pattern.compile("^[a-zA-Z0-9_+-.]+@haw-hamburg\\.de$");

    /* ---- Member Fields ---- */

    private String emailAddress;

    /* ---- Constructors ---- */

    private EmailType(){}

    public EmailType(String emailAddress){
        String lowercaseMailAddress = emailAddress.toLowerCase();

        if (!isValid(lowercaseMailAddress)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        this.emailAddress = lowercaseMailAddress;
    }

    /* ---- Getters/Setters ---- */

    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public String getEmailAddress() {
        return emailAddress;
    }

    /* ---- Overridden Methods ---- */

    @Override
    public String toString() {
        return this.emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailType that = (EmailType) o;
        return Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }

    /* ---- Custom Methods ---- */

    public static boolean isValid(String emailAddress){
        if (emailAddress == null) {
            return false;
        } else {
            return EMAIL_TYPE_PATTERN.matcher(emailAddress.toLowerCase()).matches();
        }
    }
}
