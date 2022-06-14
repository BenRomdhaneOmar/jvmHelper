package com.omar.ben.romdhane.jvmhelper.monad

import java.util.*
import kotlin.random.Random
import kotlin.test.*

internal class EitherKotlinTest {

    @Test
    fun `either must return true when is right is called if it was created as right either`() {
        val result =
            Either.right<Any, Int>(Random.nextInt())
                .isRight()

        assertTrue(result)
    }

    @Test
    fun `either must return false when is right is called if it was created as left either`() {
        val result =
            Either.left<Int, Any>(Random.nextInt())
                .isRight()

        assertFalse(result)
    }

    @Test
    fun `either must return true when is left is called if it was created as left either`() {
        val result =
            Either.left<Int, Any>(Random.nextInt())
                .isLeft()

        assertTrue(result)
    }

    @Test
    fun `either must return false when is left is called if it was created as right either`() {
        val result =
            Either.right<Any, Int>(Random.nextInt())
                .isLeft()

        assertFalse(result)
    }

    @Test
    fun `either must return a maybe that contains the initial value when right is called if it was created as right either`() {
        val initialValue = Random.nextInt()
        val result =
            Either.right<Any, Int>(initialValue)
                .right()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either must return empty maybe when right is called if it was created as left either`() {
        val result =
            Either.left<Int, Any>(Random.nextInt())
                .right()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `either must return a maybe that contains the initial value when left is called if it was created as left`() {
        val initialValue = Random.nextInt()
        val result =
            Either.left<Int, Any>(initialValue)
                .left()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either must return an empty maybe when left is called if it was created as right either`() {
        val result =
            Either.right<Any, Int>(Random.nextInt())
                .left()
                .isEmpty()

        assertTrue(result)
    }

    @Test
    fun `either must return a mapped right value when map right is called if it was created as right`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Either.right<Any, String>(initialValue)
                .mapRight(String::length)
                .right()
                .orNull()!!

        assertNotEquals<Any>(initialValue, result)
    }

    @Test
    fun `either must return a mapped either when map right is called if it was created as left either`() {
        val initialEither = Either.left<Int, String>(Random.nextInt())
        val result = initialEither.mapRight(String::length)

        assertNotEquals<Either<*, *>>(initialEither, result)
    }

    @Test
    fun `either must return a mapped value when map left called if it was created as left either`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Either.left<String, Any>(initialValue)
                .mapLeft(String::length)
                .left()
                .orNull()!!

        assertNotEquals<Any>(initialValue, result)
    }

    @Test
    fun `either must return a mapped either when map left is called if it was created as right either`() {
        val initialEither = Either.right<String, Int>(Random.nextInt())
        val result = initialEither.mapLeft(String::length)

        assertNotEquals<Either<*, *>>(initialEither, result)
    }

    @Test
    fun `either must return a right either with initial value when switch to right without condition is called if it was created as right either`() {
        val initialValue = Random.nextInt()
        val result =
            Either.right<String, Int>(initialValue)
                .switchToRight(String::length)
                .right()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either must return a right either with mapped left to right when switch to right without condition is called if it was created as left`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Either.left<String, Int>(initialValue)
                .switchToRight(String::length)
                .right()
                .orNull()

        assertEquals(initialValue.length, result)
    }

    @Test
    fun `either must return a right either with initial value when switch to right with condition that return true called if it was created as right either`() {
        val initialValue = Random.nextInt()
        val result =
            Either.right<String, Int>(initialValue)
                .switchToRight(
                    { true },
                    String::length
                )
                .right()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either must return a right either with initial value when switch to right with condition that return false called if it was created as right either`() {
        val initialValue = Random.nextInt()
        val result =
            Either.right<String, Int>(initialValue)
                .switchToRight(
                    { false },
                    String::length
                )
                .right()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either must return a right either with mapped left value when switch to right with condition that return true called if it was created as left either`() {
        val initialValue = UUID.randomUUID().toString()
        val result =
            Either.left<String, Int>(initialValue)
                .switchToRight(
                    { true },
                    String::length
                )
                .right()
                .orNull()!!

        assertEquals(initialValue.length, result)
    }

    @Test
    fun `either must return a left either with initial value when switch to right with condition that return false called if it was created as left either`() {
        val initialValue = Random.nextInt()
        val result =
            Either.left<Int, String>(initialValue)
                .switchToRight(
                    { false },
                    Int::toString
                )
                .left()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun ``() {

    }

}