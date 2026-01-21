package de.ait.validation;

import java.util.List;

public interface Validator<T> {
    boolean isValid(T object);
    List<String> validateWithErrors(T object);
}
