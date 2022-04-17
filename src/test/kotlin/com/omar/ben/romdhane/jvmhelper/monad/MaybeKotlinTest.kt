package com.omar.ben.romdhane.jvmhelper.monad

import com.omar.ben.romdhane.jvmhelper.monad.Maybe.Builder.flatten
import com.omar.ben.romdhane.jvmhelper.monad.Maybe.Builder.toMaybe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.util.*
import kotlin.random.Random
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class MaybeKotlinTest {

    @Test
    fun `maybe should return true when is present called when it is created as non empty`() {
        val result =
            Maybe.of(Random.nextInt())
                .isPresent()

        assertTrue(result)
    }

    @Test
    fun `maybe should return false when is present called when it is created as empty`() {
        val result =
            Maybe.empty<Int>()
                .isPresent()

        assertFalse(result)
    }

    @Test
    fun `maybe should return false when is empty called when it is created as non empty`() {
        val result =
            Maybe.of(Random.nextInt())
                .isEmpty()

        assertFalse(result)
    }

    @Test
    fun `maybe should return true when is empty called when it is created as empty`() {
        val result =
            Maybe.empty<Int>()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must return a mapped value when we apply map if it was created as non empty`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Maybe.of(initialValue)
                .map(String::length)
                .orNull()!!

        assertNotEquals<Any>(initialValue, result)
    }

    @Test
    fun `maybe must return a mapped maybe when we apply map if it was created as empty`() {
        val initialMaybe = Maybe.empty<String>()
        val result =
            initialMaybe
                .map(String::length)

        assertNotEquals<Maybe<*>>(initialMaybe, result)
    }

    @Test
    fun `maybe must return a mapped value when we apply flat map if it was created as non empty`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Maybe.of(initialValue)
                .flatMap { Maybe of it.length }
                .orNull()!!

        assertNotEquals<Any>(initialValue, result)
    }

    @Test
    fun `maybe must return a mapped maybe when we apply flat map if it was created as empty`() {
        val initialMaybe = Maybe.empty<String>()
        val result =
            initialMaybe
                .flatMap { Maybe of it.length }

        assertNotEquals<Maybe<*>>(initialMaybe, result)
    }

    @Test
    fun `maybe must return a maybe and not maybe of maybe when we apply flat map if it was created as non empty`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Maybe.of(initialValue)
                .flatMap { Maybe of it.length }
                .orNull()!!

        assertEquals(initialValue.length, result)
    }

    @Test
    fun `maybe must return a maybe and not maybe of maybe when we apply flat map if it was created as empty`() {
        val initialMaybe = Maybe.empty<String>()
        val result =
            initialMaybe
                .flatMap { Maybe of it.length }
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must return null when or null called if it was created as empty`() {
        val result =
            Maybe.empty<Int>()
                .orNull()

        assertEquals(result, null)
    }

    @Test
    fun `maybe must return initial value when or null called if it was created as non empty`() {
        val initialValue = Random.nextInt()
        val result =
            Maybe.of(initialValue)
                .orNull()

        assertEquals(result, initialValue)
    }

    @Test
    fun `maybe must return the alternative value when or else is called if it was created as empty`() {
        val alternativeValue = Random.nextInt()
        val result =
            Maybe.empty<Int>() orElse alternativeValue

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `maybe must return the initial value when or else is called if it was created as non empty`() {
        val initialValue = Random.nextInt()
        val result =
            Maybe of initialValue orElse Random.nextInt()

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe must return the alternative value when or else with supplier is called if it was created as empty`() {
        val alternativeSupplier = { 5 }
        val result =
            Maybe.empty<Int>()
                .orElse(alternativeSupplier)

        assertEquals(alternativeSupplier(), result)
    }

    @Test
    fun `maybe must return the initial value when or else with supplier is called if it was created as non empty`() {
        val initialValue = Random.nextInt()
        val result =
            Maybe.of(initialValue)
                .orElse { Random.nextInt() }

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe must return a non empty optional with same initial value if it was created as non empty`() {
        val initialValue = Random.nextInt()
        val result =
            Maybe.of(initialValue)
                .toOptional()
                .get()

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe must return an empty optional if it was created as empty`() {
        val result =
            Maybe.empty<Int>()
                .toOptional()
                .isEmpty

        assertTrue(result)
    }

    @Test
    fun `maybe must return a mapped value when we apply map suspend if it was created as non empty`() = runTest {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Maybe.of(initialValue)
                .mapSuspend { it.length }
                .orNull()!!

        assertNotEquals<Any>(initialValue, result)
    }

    @Test
    fun `maybe must return a mapped maybe when we apply map suspend if it was created as empty`() = runTest {
        val initialMaybe = Maybe.empty<String>()
        val result =
            initialMaybe
                .mapSuspend { it.length }

        assertNotEquals<Maybe<*>>(initialMaybe, result)
    }

    @Test
    fun `maybe must return a mapped value when we apply flat map suspend if it was created as non empty`() = runTest {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Maybe.of(initialValue)
                .flatMapSuspend { Maybe of it.length }
                .orNull()!!

        assertNotEquals<Any>(initialValue, result)
    }

    @Test
    fun `maybe must return a mapped maybe when we apply flat map suspend if it was created as empty`() = runTest {
        val initialMaybe = Maybe.empty<String>()
        val result =
            initialMaybe
                .flatMapSuspend { Maybe of it.length }

        assertNotEquals<Maybe<*>>(initialMaybe, result)
    }

    @Test
    fun `maybe must return a maybe and not maybe of maybe when we apply flat map suspend if it was created as non empty`() =
        runTest {
            val initialValue = UUID.randomUUID().toString()
            val result =
                Maybe.of(initialValue)
                    .flatMapSuspend { Maybe of it.length }
                    .orNull()!!

            assertEquals(initialValue.length, result)
        }

    @Test
    fun `maybe must return a maybe and not maybe of maybe when we apply flat map suspend if it was created as empty`() =
        runTest {
            val initialMaybe = Maybe.empty<String>()
            val result =
                initialMaybe
                    .flatMapSuspend { Maybe of it.length }
                    .isEmpty()

            assertTrue(result)
        }

    @Test
    fun `maybe must return the alternative value when or else suspend is called if it was created as empty`() =
        runTest {
            val alternativeSupplier = suspend { 5 }
            val result =
                Maybe.empty<Int>()
                    .orElseSuspend(alternativeSupplier)

            assertEquals(alternativeSupplier(), result)
        }

    @Test
    fun `maybe must return the initial value when or else suspend is called if it was created as non empty`() =
        runTest {
            val initialValue = Random.nextInt()
            val result =
                Maybe.of(initialValue)
                    .orElseSuspend { Random.nextInt() }

            assertEquals(initialValue, result)
        }

    @Test
    fun `maybe must be created with the initial value if a to maybe is called on a non empty optional not initialized with null`() {
        val initialValue = Random.nextInt()
        val result =
            Optional.of(initialValue)
                .toMaybe()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe must be created empty if a to maybe is called on a non empty optional initialized with null`() {
        val result =
            Optional.ofNullable(null)
                .toMaybe()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must be created empty if a to maybe is called on a empty optional`() {
        val result =
            Optional.empty<Int>()
                .toMaybe()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must be created with initial value if to maybe is called on non nullable object`() {
        val initialValue = Random.nextInt()
        val result =
            initialValue.toMaybe()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe must be created with initial value if to maybe is called on nullable object not null`() {
        val initialValue: Int? = Random.nextInt()
        val result =
            initialValue.toMaybe()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe must be created empty if to maybe is called on nullable object null`() {
        val result =
            null.toMaybe()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe of maybe must return a maybe and not maybe of maybe when we apply flatten if it was created as non empty`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Maybe.of(Maybe of initialValue)
                .flatten()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `maybe of maybe must return a maybe and not maybe of maybe when we apply flat map if it was created as maybe of empty`() {
        val result =
            Maybe.of(Maybe.empty<String>())
                .flatten()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe of maybe must return a maybe and not maybe of maybe when we apply flat map if it was created as empty`() {
        val result =
            Maybe.empty<Maybe<String>>()
                .flatten()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must be created empty if it was created by empty optional`() {
        val result =
            Maybe.fromOptional(Optional.empty())
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must be created empty if it was created by optional contains null`() {
        val result =
            Maybe.fromOptional(Optional.ofNullable(null))
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `maybe must be created with initial value if it was created by optional non empty and contain non null element`() {
        val initialValue = Random.nextInt()
        val result =
            Maybe.fromOptional(Optional.of(initialValue))
                .orNull()!!

        assertEquals(initialValue, result)
    }

}