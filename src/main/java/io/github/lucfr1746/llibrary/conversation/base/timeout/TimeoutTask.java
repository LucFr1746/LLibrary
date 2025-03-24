package io.github.lucfr1746.llibrary.conversation.base.timeout;

/**
 * Represents a basic timeout task.
 *
 * @author MrIvanPlays
 */
public interface TimeoutTask {

    /**
     * Returns whether the task was called.
     *
     * @return called or not
     */
    boolean hasCalled();

    /**
     * Cancel this timeout task if not canceled already.
     */
    void cancel();
}
