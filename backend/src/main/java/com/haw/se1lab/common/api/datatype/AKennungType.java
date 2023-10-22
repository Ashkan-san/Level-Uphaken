package com.haw.se1lab.common.api.datatype;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * Fachlicher Datentyp A-Kennung
 *
 * @author Kjell May
 */
@SuppressWarnings("JpaObjectClassSignatureInspection")
@Embeddable
public class AKennungType {

    private static final String AKENNUNG_PATTERN = "a\\w{2}\\d{3}";

    private String aKennung;

    public AKennungType(String aKennung) {
        if (!aKennung.matches(AKENNUNG_PATTERN)) throw new IllegalArgumentException("Ungültige A-Kennung!");
        this.aKennung = aKennung;
    }

    private AKennungType() {

    }

    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public String getaKennung() {
        return aKennung;
    }

    @Override
    public String toString() {
        return aKennung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AKennungType that = (AKennungType) o;
        return Objects.equals(aKennung, that.aKennung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aKennung);
    }
}
