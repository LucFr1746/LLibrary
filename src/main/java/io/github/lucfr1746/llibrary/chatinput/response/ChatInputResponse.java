package io.github.lucfr1746.llibrary.chatinput.response;

import io.github.lucfr1746.llibrary.chatinput.response.enums.InputStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A class that represents the response of an input prompt.
 * It contains the status and the value.
 * */
public record ChatInputResponse(InputStatus status, String value) {

    /**
     * Returns the status of an input prompt.
     *
     * @return the status
     * */
    @NotNull
    public InputStatus status() {
        return status;
    }

    /**
     * Returns the string value of an input prompt.
     *
     * @return the value
     * */
    @NotNull
    public String value() {
        return value;
    }
}
