package io.github.lucfr1746.llibrary.chatinput.input;

import org.jetbrains.annotations.NotNull;

/**
 * A child class of AquaInput used for the input of non-empty strings.
 * */
public class StringInput extends ChatInput {

    @Override
    public boolean isValidInput(@NotNull String input) {
        return !input.isEmpty();
    }
}
