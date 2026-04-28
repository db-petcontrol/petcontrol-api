package br.com.db.petcontrol.exception;

import java.util.List;

public class NotFoundException extends RuntimeException {
  private final List<String> messages;

  public NotFoundException(String message) {
    super(message);
    this.messages = List.of(message);
  }

  public NotFoundException(List<String> messages) {
    super(messages.isEmpty() ? "Not found" : messages.get(0));
    this.messages = messages;
  }

  public List<String> getMessages() {
    return messages;
  }
}
