package io.github.lucfr1746.llibrary.chatinput.request;

import io.github.lucfr1746.llibrary.chatinput.input.ChatInput;
import io.github.lucfr1746.llibrary.chatinput.response.ChatInputResponse;

import java.util.concurrent.CompletableFuture;

public class ChatInputRequest {
    private final CompletableFuture<ChatInputResponse> future = new CompletableFuture<>();
    private final ChatInput input;

    public ChatInputRequest(ChatInput input) {
        this.input = input;
    }

    public CompletableFuture<ChatInputResponse> getFuture() {
        return future;
    }

    public ChatInput getInput() {
        return input;
    }
}
