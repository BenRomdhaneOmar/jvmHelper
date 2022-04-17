package com.omar.ben.romdhane.jvmhelper.monad

import com.omar.ben.romdhane.jvmhelper.monad.Maybe.Builder.empty
import com.omar.ben.romdhane.jvmhelper.monad.Maybe.Builder.toMaybe
import java.util.function.Function
import java.util.function.Predicate

sealed interface Either<LEFT : Any, RIGHT : Any> {

    fun isRight(): Boolean
    fun isLeft(): Boolean
    fun right(): Maybe<RIGHT>
    fun left(): Maybe<LEFT>
    fun <MAPPED : Any> mapRight(mapper: Function<RIGHT, MAPPED>): Either<LEFT, MAPPED>
    fun <MAPPED : Any> mapLeft(mapper: Function<LEFT, MAPPED>): Either<MAPPED, RIGHT>
    fun switchToRight(switch: Function<LEFT, RIGHT>): Either<LEFT, RIGHT>
    fun switchToRight(
        condition: Predicate<LEFT> = Predicate { true },
        switch: Function<LEFT, RIGHT>
    ): Either<LEFT, RIGHT>

    fun switchToLeft(switch: Function<RIGHT, LEFT>): Either<LEFT, RIGHT>
    fun switchToLeft(
        condition: Predicate<RIGHT> = Predicate { true },
        switch: Function<RIGHT, LEFT>
    ): Either<LEFT, RIGHT>

    fun <RESULT : Any> fold(rightMapper: Function<RIGHT, RESULT>, leftMapper: Function<LEFT, RESULT>): RESULT
    override fun toString(): String

    @JvmSynthetic
    suspend fun <MAPPED : Any> mapRightSuspend(mapper: suspend (RIGHT) -> MAPPED): Either<LEFT, MAPPED>

    @JvmSynthetic
    suspend fun <MAPPED : Any> mapLeftSuspend(mapper: suspend (LEFT) -> MAPPED): Either<MAPPED, RIGHT>

    @JvmSynthetic
    suspend fun switchToRightSuspend(switch: suspend (LEFT) -> RIGHT): Either<LEFT, RIGHT>

    @JvmSynthetic
    suspend fun switchToRightSuspend(
        condition: (LEFT) -> Boolean = { true },
        switch: suspend (LEFT) -> RIGHT
    ): Either<LEFT, RIGHT>

    @JvmSynthetic
    suspend fun switchToLeftSuspend(switch: suspend (RIGHT) -> LEFT): Either<LEFT, RIGHT>

    @JvmSynthetic
    suspend fun switchToLeftSuspend(
        condition: (RIGHT) -> Boolean = { true },
        switch: suspend (RIGHT) -> LEFT
    ): Either<LEFT, RIGHT>

    @JvmSynthetic
    suspend fun <RESULT : Any> foldSuspend(
        rightMapper: suspend (RIGHT) -> RESULT,
        leftMapper: suspend (LEFT) -> RESULT
    ): RESULT

    companion object Builder {

        @JvmStatic
        infix fun <LEFT : Any, RIGHT : Any> right(element: RIGHT): Either<LEFT, RIGHT> = Right of element

        @JvmStatic
        infix fun <LEFT : Any, RIGHT : Any> left(element: LEFT): Either<LEFT, RIGHT> = Left of element

        fun <RIGHT : Any> Exception.toEither(): Either<Exception, RIGHT> = left(this)

        fun <LEFT : Any, TYPE : Any> TYPE.toRightEither(): Either<LEFT, TYPE> = right(this)

        fun <TYPE : Any, RIGHT : Any> TYPE.toLeftEither(): Either<TYPE, RIGHT> = left(this)

    }

    private class Right<LEFT : Any, RIGHT : Any> private constructor(private val element: RIGHT) : Either<LEFT, RIGHT> {

        companion object Builder {

            infix fun <LEFT : Any, RIGHT : Any> of(element: RIGHT) = Right<LEFT, RIGHT>(element)

        }

        override fun isRight() = true

        override fun isLeft() = false

        override fun right() = element.toMaybe()

        override fun left() = empty<LEFT>()

        override suspend fun <RESULT : Any> foldSuspend(
            rightMapper: suspend (RIGHT) -> RESULT,
            leftMapper: suspend (LEFT) -> RESULT
        ) = rightMapper(element)

        override suspend fun switchToLeftSuspend(
            condition: (RIGHT) -> Boolean,
            switch: suspend (RIGHT) -> LEFT
        ) =
            if (condition(element))
                switchToLeftSuspend(switch)
            else
                this

        override suspend fun switchToLeftSuspend(switch: suspend (RIGHT) -> LEFT) =
            switch(element).toLeftEither<LEFT, RIGHT>()

        override suspend fun switchToRightSuspend(
            condition: (LEFT) -> Boolean,
            switch: suspend (LEFT) -> RIGHT
        ) = this

        override suspend fun switchToRightSuspend(switch: suspend (LEFT) -> RIGHT) = this

        override suspend fun <MAPPED : Any> mapLeftSuspend(mapper: suspend (LEFT) -> MAPPED) =
            of<MAPPED, RIGHT>(element)

        override suspend fun <MAPPED : Any> mapRightSuspend(mapper: suspend (RIGHT) -> MAPPED) =
            of<LEFT, MAPPED>(mapper(element))

        override fun <RESULT : Any> fold(
            rightMapper: Function<RIGHT, RESULT>,
            leftMapper: Function<LEFT, RESULT>
        ) = rightMapper.apply(element)

        override fun switchToLeft(condition: Predicate<RIGHT>, switch: Function<RIGHT, LEFT>) =
            if (condition.test(element))
                switchToLeft(switch)
            else
                this

        override fun switchToLeft(switch: Function<RIGHT, LEFT>) = switch.apply(element).toLeftEither<LEFT, RIGHT>()

        override fun switchToRight(condition: Predicate<LEFT>, switch: Function<LEFT, RIGHT>) = this

        override fun switchToRight(switch: Function<LEFT, RIGHT>) = this

        override fun <MAPPED : Any> mapLeft(mapper: Function<LEFT, MAPPED>) = of<MAPPED, RIGHT>(element)

        override fun <MAPPED : Any> mapRight(mapper: Function<RIGHT, MAPPED>) = of<LEFT, MAPPED>(mapper.apply(element))

        override fun toString() = """Either.Right(element=$element)"""

    }

    private class Left<LEFT : Any, RIGHT : Any> private constructor(private val element: LEFT) : Either<LEFT, RIGHT> {

        companion object Builder {

            infix fun <LEFT : Any, RIGHT : Any> of(element: LEFT) = Left<LEFT, RIGHT>(element)

        }

        override fun isRight() = false

        override fun isLeft() = true

        override fun right() = empty<RIGHT>()

        override fun left() = element.toMaybe()

        override suspend fun <RESULT : Any> foldSuspend(
            rightMapper: suspend (RIGHT) -> RESULT,
            leftMapper: suspend (LEFT) -> RESULT
        ) = leftMapper(element)

        override suspend fun switchToLeftSuspend(
            condition: (RIGHT) -> Boolean,
            switch: suspend (RIGHT) -> LEFT
        ) = this

        override suspend fun switchToLeftSuspend(switch: suspend (RIGHT) -> LEFT) = this

        override suspend fun switchToRightSuspend(
            condition: (LEFT) -> Boolean,
            switch: suspend (LEFT) -> RIGHT
        ) =
            if (condition(element))
                switchToRightSuspend(switch)
            else
                this

        override suspend fun switchToRightSuspend(switch: suspend (LEFT) -> RIGHT) =
            switch(element).toRightEither<LEFT, RIGHT>()

        override suspend fun <MAPPED : Any> mapLeftSuspend(mapper: suspend (LEFT) -> MAPPED) =
            of<MAPPED, RIGHT>(mapper(element))

        override suspend fun <MAPPED : Any> mapRightSuspend(mapper: suspend (RIGHT) -> MAPPED) =
            of<LEFT, MAPPED>(element)

        override fun <RESULT : Any> fold(
            rightMapper: Function<RIGHT, RESULT>,
            leftMapper: Function<LEFT, RESULT>
        ) = leftMapper.apply(element)

        override fun switchToLeft(condition: Predicate<RIGHT>, switch: Function<RIGHT, LEFT>) = this

        override fun switchToLeft(switch: Function<RIGHT, LEFT>) = this

        override fun switchToRight(condition: Predicate<LEFT>, switch: Function<LEFT, RIGHT>) =
            if (condition.test(element))
                switchToRight(switch)
            else
                this

        override fun switchToRight(switch: Function<LEFT, RIGHT>) = switch.apply(element).toRightEither<LEFT, RIGHT>()

        override fun <MAPPED : Any> mapLeft(mapper: Function<LEFT, MAPPED>) = of<MAPPED, RIGHT>(mapper.apply(element))

        override fun <MAPPED : Any> mapRight(mapper: Function<RIGHT, MAPPED>) = of<LEFT, MAPPED>(element)

        override fun toString() = """Either.Left(element=$element)"""

    }

}