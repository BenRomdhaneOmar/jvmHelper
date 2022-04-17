package com.omar.ben.romdhane.jvmhelper.monad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class MaybeJavaTest {

    @Test
    @DisplayName("maybe should return true when is present called when it is created as non empty")
    void maybe_should_return_true_when_is_present_called_when_it_is_created_as_non_empty() {
        final var result =
                Maybe.of(new Random().nextInt())
                        .isPresent();

        assertTrue(result);
    }

    @Test
    @DisplayName("maybe should return false when is present called when it is created as empty")
    void maybe_should_return_false_when_is_present_called_when_it_is_created_as_empty() {
        final var result =
                Maybe.empty()
                        .isPresent();

        assertFalse(result);
    }

    @Test
    @DisplayName("maybe should return false when is empty called when it is created as non empty")
    void maybe_should_return_false_when_is_empty_called_when_it_is_created_as_non_empty() {
        final var result =
                Maybe.of(new Random().nextInt())
                        .isEmpty();

        assertFalse(result);
    }

    @Test
    @DisplayName("maybe should return true when is empty called when it is created as empty")
    void maybe_should_return_true_when_is_empty_called_when_it_is_created_as_empty() {
        final var result =
                Maybe.empty()
                        .isEmpty();

        assertTrue(result);
    }

    @Test
    @DisplayName("maybe must return a mapped value when we apply map if it was created as non empty")
    void maybe_must_return_a_mapped_value_when_we_apply_map_if_it_was_created_as_non_empty() {
        final var initialValue = UUID.randomUUID().toString();
        final var result =
                Maybe.of(initialValue)
                        .map(String::length)
                        .orElse(new Random().nextInt());

        assertNotEquals(initialValue, result);
    }

    @Test
    @DisplayName("maybe must return a mapped maybe when we apply map if it was created as empty")
    void maybe_must_return_a_mapped_maybe_when_we_apply_map_if_it_was_created_as_empty() {
        final var initialMaybe = Maybe.<String>empty();
        final var result =
                initialMaybe
                        .map(String::length);

        assertNotEquals(initialMaybe, result);
    }

    @Test
    @DisplayName("maybe must return a mapped value when we apply flat map if it was created as non empty")
    void maybe_must_return_a_mapped_value_when_we_apply_flat_map_if_it_was_created_as_non_empty() {
        final var initialValue = UUID.randomUUID().toString();
        final var result =
                Maybe.of(initialValue)
                        .flatMap(it -> Maybe.of(it.length()))
                        .orElse(new Random().nextInt());

        assertNotEquals(initialValue, result);
    }

    @Test
    @DisplayName("maybe must return a mapped maybe when we apply flat map if it was created as empty")
    void maybe_must_return_a_mapped_maybe_when_we_apply_flat_map_if_it_was_created_as_empty() {
        final var initialMaybe = Maybe.<String>empty();
        final var result =
                initialMaybe
                        .flatMap(it -> Maybe.of(it.length()));

        assertNotEquals(initialMaybe, result);
    }

    @Test
    @DisplayName("maybe must return a maybe and not maybe of maybe when we apply flat map if it was created as non empty")
    void maybe_must_return_a_maybe_and_not_maybe_of_maybe_when_we_apply_flat_map_if_it_was_created_as_non_empty() {
        final var initialValue = UUID.randomUUID().toString();
        final var result =
                Maybe.of(initialValue)
                        .flatMap(it -> Maybe.of(it.length()))
                        .orElse(new Random().nextInt());

        assertEquals(initialValue.length(), result);
    }

    @Test
    @DisplayName("maybe must return a maybe and not maybe of maybe when we apply flat map if it was created as empty")
    void maybe_must_return_a_maybe_and_not_maybe_of_maybe_when_we_apply_flat_map_if_it_was_created_as_empty() {
        final var initialMaybe = Maybe.<String>empty();
        final var result =
                initialMaybe
                        .flatMap(it -> Maybe.of(it.length()))
                        .isEmpty();

        assertTrue(result);
    }

    @Test
    @DisplayName("maybe must return the alternative value when or else is called if it was created as empty")
    void maybe_must_return_the_alternative_value_when_or_else_is_called_if_it_was_created_as_empty() {
        final var alternativeValue = new Random().nextInt();
        final var result =
                Maybe.<Integer>empty()
                        .orElse(alternativeValue);

        assertEquals(alternativeValue, result);
    }

    @Test
    @DisplayName("maybe must return the initial value when or else is called if it was created as non empty")
    void maybe_must_return_the_initial_value_when_or_else_is_called_if_it_was_created_as_non_empty() {
        final var initialValue = new Random().nextInt();
        final var result =
                Maybe.of(initialValue)
                        .orElse(new Random().nextInt());

        assertEquals(initialValue, result);
    }

    @Test
    @DisplayName("maybe must return the alternative value when or else with supplier is called if it was created as empty")
    void maybe_must_return_the_alternative_value_when_or_else_with_supplier_is_called_if_it_was_created_as_empty() {
        final Supplier<Integer> alternativeSupplier = (() -> 5);
        final var result =
                Maybe.<Integer>empty()
                        .orElse(alternativeSupplier);

        assertEquals(alternativeSupplier.get(), result);
    }

    @Test
    @DisplayName("maybe must return the initial value when or else with supplier is called if it was created as non empty")
    void maybe_must_return_the_initial_value_when_or_else_with_supplier_is_called_if_it_was_created_as_non_empty() {
        final var initialValue = new Random().nextInt();
        final var result =
                Maybe.of(initialValue)
                        .orElse(new Random()::nextInt);

        assertEquals(initialValue, result);
    }

    @Test
    @DisplayName("maybe must return a non empty optional with same initial value if it was created as non empty")
    void maybe_must_return_a_non_empty_optional_with_same_initial_value_if_it_was_created_as_non_empty() {
        final var initialValue = new Random().nextInt();
        final var result =
                Maybe.of(initialValue)
                        .toOptional()
                        .get();

        assertEquals(initialValue, result);
    }

    @Test
    @DisplayName("maybe must return an empty optional if it was created as empty")
    void maybe_must_return_an_empty_optional_if_it_was_created_as_empty() {
        final var result =
                Maybe.<Integer>empty()
                        .toOptional()
                        .isEmpty();

        assertTrue(result);
    }

    @Test
    @DisplayName("maybe must be created empty if it was created by empty optional")
    void maybe_must_be_created_empty_if_it_was_created_by_empty_optional() {
        final var result =
                Maybe.fromOptional(Optional.empty())
                        .isEmpty();

        assertTrue(result);
    }

    @Test
    @DisplayName("maybe must be created empty if it was created by optional contains null")
    void maybe_must_be_created_empty_if_it_was_created_by_optional_contains_null() {
        final var result =
                Maybe.fromOptional(Optional.ofNullable(null))
                        .isEmpty();

        assertTrue(result);
    }

    @Test
    @DisplayName("maybe must be created with initial value if it was created by optional non empty and contain non null element")
    void maybe_must_be_created_with_initial_value_if_it_was_created_by_optional_non_empty_and_contain_non_null_element() {
        final var initialValue = new Random().nextInt();
        final var result =
                Maybe.fromOptional(Optional.of(initialValue))
                        .orElse(new Random().nextInt());

        assertEquals(initialValue, result);
    }

}
