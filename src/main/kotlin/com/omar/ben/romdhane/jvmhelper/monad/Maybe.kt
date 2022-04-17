package com.omar.ben.romdhane.jvmhelper.monad

import java.util.*
import java.util.function.Function
import java.util.function.Supplier

sealed interface Maybe<TYPE : Any> {

    fun isPresent(): Boolean
    fun isEmpty(): Boolean
    fun <MAPPED : Any> map(mapper: Function<TYPE, MAPPED>): Maybe<MAPPED>
    fun <OTHER : Any> flatMap(mapper: Function<TYPE, Maybe<OTHER>>): Maybe<OTHER>
    fun orNull(): TYPE?
    fun orElse(alternative: TYPE): TYPE
    fun orElse(alternativeSupplier: Supplier<TYPE>): TYPE
    fun toOptional(): Optional<TYPE>

    @JvmSynthetic
    suspend fun <MAPPED : Any> mapSuspend(mapper: suspend (TYPE) -> MAPPED): Maybe<MAPPED>

    @JvmSynthetic
    suspend fun <OTHER : Any> flatMapSuspend(mapper: suspend (TYPE) -> Maybe<OTHER>): Maybe<OTHER>

    @JvmSynthetic
    suspend fun orElseSuspend(alternativeSupplier: suspend () -> TYPE): TYPE

    companion object Builder {

        @JvmStatic
        fun <TYPE : Any> of(element: TYPE): Maybe<TYPE> = NotEmpty.of(element)

        @JvmStatic
        fun <TYPE : Any> empty(): Maybe<TYPE> = Empty.of()

        fun <TYPE : Any> Optional<TYPE>.toMaybe(): Maybe<TYPE> =
            map { of(it) }
                .orElse(empty())

        fun <TYPE : Any> TYPE.toMaybe(): Maybe<TYPE> =
            of(this)

        @JvmName("toMaybeOnNullable")
        fun <TYPE : Any, NULLABLE_TYPE : TYPE?> NULLABLE_TYPE.toMaybe(): Maybe<TYPE> =
            if (this != null)
                of(this)
            else
                empty()

        fun <TYPE : Any> Maybe<Maybe<TYPE>>.flatten(): Maybe<TYPE> =
            orElse(empty())

    }

    private class NotEmpty<TYPE : Any> private constructor(private val element: TYPE) : Maybe<TYPE> {

        companion object Builder {
            fun <TYPE : Any> of(element: TYPE) = NotEmpty(element)
        }

        override fun isPresent() = true

        override fun isEmpty() = false

        override fun <MAPPED : Any> map(mapper: Function<TYPE, MAPPED>) = of(mapper.apply(element))

        override fun <OTHER : Any> flatMap(mapper: Function<TYPE, Maybe<OTHER>>) = mapper.apply(element)

        override fun orNull() = element

        override fun orElse(alternative: TYPE) = element

        override fun orElse(alternativeSupplier: Supplier<TYPE>) = element

        override fun toOptional() = Optional.of(element)

        override suspend fun <MAPPED : Any> mapSuspend(mapper: suspend (TYPE) -> MAPPED) = of(mapper(element))

        override suspend fun <OTHER : Any> flatMapSuspend(mapper: suspend (TYPE) -> Maybe<OTHER>) = mapper(element)

        override suspend fun orElseSuspend(alternativeSupplier: suspend () -> TYPE) = element

    }

    private class Empty<TYPE : Any> private constructor() : Maybe<TYPE> {

        companion object Builder {
            fun <TYPE : Any> of() = Empty<TYPE>()
        }

        override fun isPresent() = false

        override fun isEmpty() = true

        override fun <MAPPED : Any> map(mapper: Function<TYPE, MAPPED>) = of<MAPPED>()

        override fun <OTHER : Any> flatMap(mapper: Function<TYPE, Maybe<OTHER>>) = of<OTHER>()

        override fun orNull(): TYPE? = null

        override fun orElse(alternative: TYPE) = alternative

        override fun orElse(alternativeSupplier: Supplier<TYPE>) = alternativeSupplier.get()

        override fun toOptional() = Optional.empty<TYPE>()

        override suspend fun <MAPPED : Any> mapSuspend(mapper: suspend (TYPE) -> MAPPED) = of<MAPPED>()

        override suspend fun <OTHER : Any> flatMapSuspend(mapper: suspend (TYPE) -> Maybe<OTHER>) = of<OTHER>()

        override suspend fun orElseSuspend(alternativeSupplier: suspend () -> TYPE) = alternativeSupplier()

    }
}