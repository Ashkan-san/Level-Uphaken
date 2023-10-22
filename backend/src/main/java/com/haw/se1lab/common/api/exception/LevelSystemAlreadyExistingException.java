package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the exception when a level system could not be created because it already exists.
 *
 * @author Kathleen Neitzel
 */
public class LevelSystemAlreadyExistingException extends Exception {


    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String LEVELSYSTEM_WITH_ID_ALREADY_EXISTS = "Level system with id %d already exists.";

    /* ---- Member Fields ---- */

    private int id;

    /* ---- Constructors ---- */

    public LevelSystemAlreadyExistingException(int id) {
        super(String.format(LEVELSYSTEM_WITH_ID_ALREADY_EXISTS, id));
        this.id = id;
    }

    /* ---- Getters/Setters ---- */

    public int getId() {
        return id;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */


}
