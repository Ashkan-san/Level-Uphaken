package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a level system could not be found by the given id.
 *
 * @author Kathleen Neitzel
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class LevelSystemNotFoundException extends Exception {


        /* ---- Class Fields ---- */

        private static final long serialVersionUID = 1L;

        public static final String LS_WITH_ID_NOT_FOUND_MESSAGE = "Could not find level system with ID %d.";

        /* ---- Member Fields ---- */

        private final int id;

        /* ---- Constructors ---- */

        public LevelSystemNotFoundException(int id) {
            super(String.format(LS_WITH_ID_NOT_FOUND_MESSAGE, id));
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
