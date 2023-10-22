package com.haw.se1lab.common.api.datatype;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Repräsentiert Passwort Datentyp.
 *
 * @author Isabell Schettler
 * @author Philip Gisella
 *
 */
@SuppressWarnings("JpaObjectClassSignatureInspection")
@Embeddable
public class Password {
    /* ---- Class Fields ---- */

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$");

    /* ---- Member Fields ---- */
    private String password;

    /* ---- Constructors ---- */

    private Password(){}

    public Password(String password){
        if (!isValid(password)) {
            throw new IllegalArgumentException("Invalid password!");
        }

        this.password = password;
    }

    /* ---- Getters/Setters ---- */

    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public String getPassword() {
        return password;
    }

    /* ---- Overridden Methods ---- */

    @Override
    public String toString() {
        return "Password: '" + password + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    /* ---- Custom Methods ---- */

    public static boolean isValid(String password){
        if (password == null) {
            return false;
        } else {
            return PASSWORD_PATTERN.matcher(password).matches();
        }
    }
}
