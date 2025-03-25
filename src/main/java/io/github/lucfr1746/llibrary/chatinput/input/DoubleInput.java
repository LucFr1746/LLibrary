package io.github.lucfr1746.llibrary.chatinput.input;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A child class of ChatInput used for the input of double values.
 * */
public class DoubleInput extends ChatInput {

    @Override
    public boolean isValidInput(@NotNull String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses the given input if possible into a double.
     *
     * @param input the input
     * @return an optional that may contain the parsed value
     * */
    public Optional<Double> parseInput(@NotNull String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
