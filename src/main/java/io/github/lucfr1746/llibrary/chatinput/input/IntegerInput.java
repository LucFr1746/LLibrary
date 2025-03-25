package io.github.lucfr1746.llibrary.chatinput.input;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A child class of AquaInput used for the input of integer values.
 * */
public class IntegerInput extends ChatInput {

    @Override
    public boolean isValidInput(@NotNull String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses the given input if possible into an integer.
     *
     * @param input the input
     * @return an optional that may contain the parsed value
     * */
    public Optional<Integer> parseInput(@NotNull String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
