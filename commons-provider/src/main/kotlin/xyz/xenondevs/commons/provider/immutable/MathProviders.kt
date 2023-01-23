package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

//<editor-fold desc="Addition">
@JvmName("bytePlusByte")
operator fun Provider<Byte>.plus(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("bytePlusShort")
operator fun Provider<Byte>.plus(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("bytePlusInt")
operator fun Provider<Byte>.plus(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("bytePlusLong")
operator fun Provider<Byte>.plus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("bytePlusFloat")
operator fun Provider<Byte>.plus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("bytePlusDouble")
operator fun Provider<Byte>.plus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("shortPlusByte")
operator fun Provider<Short>.plus(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("shortPlusShort")
operator fun Provider<Short>.plus(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("shortPlusInt")
operator fun Provider<Short>.plus(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("shortPlusLong")
operator fun Provider<Short>.plus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("shortPlusFloat")
operator fun Provider<Short>.plus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("shortPlusDouble")
operator fun Provider<Short>.plus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("intPlusByte")
operator fun Provider<Int>.plus(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("intPlusShort")
operator fun Provider<Int>.plus(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("intPlusInt")
operator fun Provider<Int>.plus(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("intPlusLong")
operator fun Provider<Int>.plus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("intPlusFloat")
operator fun Provider<Int>.plus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("intPlusDouble")
operator fun Provider<Int>.plus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("longPlusByte")
operator fun Provider<Long>.plus(other: Provider<Byte>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("longPlusShort")
operator fun Provider<Long>.plus(other: Provider<Short>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("longPlusInt")
operator fun Provider<Long>.plus(other: Provider<Int>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("longPlusLong")
operator fun Provider<Long>.plus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("longPlusFloat")
operator fun Provider<Long>.plus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("longPlusDouble")
operator fun Provider<Long>.plus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("floatPlusByte")
operator fun Provider<Float>.plus(other: Provider<Byte>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("floatPlusShort")
operator fun Provider<Float>.plus(other: Provider<Short>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("floatPlusInt")
operator fun Provider<Float>.plus(other: Provider<Int>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("floatPlusLong")
operator fun Provider<Float>.plus(other: Provider<Long>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("floatPlusFloat")
operator fun Provider<Float>.plus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("floatPlusDouble")
operator fun Provider<Float>.plus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("doublePlusByte")
operator fun Provider<Double>.plus(other: Provider<Byte>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("doublePlusShort")
operator fun Provider<Double>.plus(other: Provider<Short>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("doublePlusInt")
operator fun Provider<Double>.plus(other: Provider<Int>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("doublePlusLong")
operator fun Provider<Double>.plus(other: Provider<Long>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("doublePlusFloat")
operator fun Provider<Double>.plus(other: Provider<Float>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }

@JvmName("doublePlusDouble")
operator fun Provider<Double>.plus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a + b }
//</editor-fold>

//<editor-fold desc="Subtraction">
@JvmName("byteMinusByte")
operator fun Provider<Byte>.minus(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("byteMinusShort")
operator fun Provider<Byte>.minus(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("byteMinusInt")
operator fun Provider<Byte>.minus(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("byteMinusLong")
operator fun Provider<Byte>.minus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("byteMinusFloat")
operator fun Provider<Byte>.minus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("byteMinusDouble")
operator fun Provider<Byte>.minus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("shortMinusByte")
operator fun Provider<Short>.minus(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("shortMinusShort")
operator fun Provider<Short>.minus(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("shortMinusInt")
operator fun Provider<Short>.minus(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("shortMinusLong")
operator fun Provider<Short>.minus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("shortMinusFloat")
operator fun Provider<Short>.minus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("shortMinusDouble")
operator fun Provider<Short>.minus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("intMinusByte")
operator fun Provider<Int>.minus(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("intMinusShort")
operator fun Provider<Int>.minus(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("intMinusInt")
operator fun Provider<Int>.minus(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("intMinusLong")
operator fun Provider<Int>.minus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("intMinusFloat")
operator fun Provider<Int>.minus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("intMinusDouble")
operator fun Provider<Int>.minus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("longMinusByte")
operator fun Provider<Long>.minus(other: Provider<Byte>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("longMinusShort")
operator fun Provider<Long>.minus(other: Provider<Short>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("longMinusInt")
operator fun Provider<Long>.minus(other: Provider<Int>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("longMinusLong")
operator fun Provider<Long>.minus(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("longMinusFloat")
operator fun Provider<Long>.minus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("longMinusDouble")
operator fun Provider<Long>.minus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("floatMinusByte")
operator fun Provider<Float>.minus(other: Provider<Byte>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("floatMinusShort")
operator fun Provider<Float>.minus(other: Provider<Short>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("floatMinusInt")
operator fun Provider<Float>.minus(other: Provider<Int>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("floatMinusLong")
operator fun Provider<Float>.minus(other: Provider<Long>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("floatMinusFloat")
operator fun Provider<Float>.minus(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("floatMinusDouble")
operator fun Provider<Float>.minus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("doubleMinusByte")
operator fun Provider<Double>.minus(other: Provider<Byte>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("doubleMinusShort")
operator fun Provider<Double>.minus(other: Provider<Short>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("doubleMinusInt")
operator fun Provider<Double>.minus(other: Provider<Int>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("doubleMinusLong")
operator fun Provider<Double>.minus(other: Provider<Long>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("doubleMinusFloat")
operator fun Provider<Double>.minus(other: Provider<Float>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }

@JvmName("doubleMinusDouble")
operator fun Provider<Double>.minus(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a - b }
//</editor-fold>

//<editor-fold desc="Multiplication">
@JvmName("byteTimesByte")
operator fun Provider<Byte>.times(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("byteTimesShort")
operator fun Provider<Byte>.times(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("byteTimesInt")
operator fun Provider<Byte>.times(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("byteTimesLong")
operator fun Provider<Byte>.times(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("byteTimesFloat")
operator fun Provider<Byte>.times(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("byteTimesDouble")
operator fun Provider<Byte>.times(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("shortTimesByte")
operator fun Provider<Short>.times(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("shortTimesShort")
operator fun Provider<Short>.times(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("shortTimesInt")
operator fun Provider<Short>.times(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("shortTimesLong")
operator fun Provider<Short>.times(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("shortTimesFloat")
operator fun Provider<Short>.times(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("shortTimesDouble")
operator fun Provider<Short>.times(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("intTimesByte")
operator fun Provider<Int>.times(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("intTimesShort")
operator fun Provider<Int>.times(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("intTimesInt")
operator fun Provider<Int>.times(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("intTimesLong")
operator fun Provider<Int>.times(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("intTimesFloat")
operator fun Provider<Int>.times(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("intTimesDouble")
operator fun Provider<Int>.times(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("longTimesByte")
operator fun Provider<Long>.times(other: Provider<Byte>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("longTimesShort")
operator fun Provider<Long>.times(other: Provider<Short>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("longTimesInt")
operator fun Provider<Long>.times(other: Provider<Int>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("longTimesLong")
operator fun Provider<Long>.times(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("longTimesFloat")
operator fun Provider<Long>.times(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("longTimesDouble")
operator fun Provider<Long>.times(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("floatTimesByte")
operator fun Provider<Float>.times(other: Provider<Byte>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("floatTimesShort")
operator fun Provider<Float>.times(other: Provider<Short>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("floatTimesInt")
operator fun Provider<Float>.times(other: Provider<Int>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("floatTimesLong")
operator fun Provider<Float>.times(other: Provider<Long>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("floatTimesFloat")
operator fun Provider<Float>.times(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("floatTimesDouble")
operator fun Provider<Float>.times(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("doubleTimesByte")
operator fun Provider<Double>.times(other: Provider<Byte>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("doubleTimesShort")
operator fun Provider<Double>.times(other: Provider<Short>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("doubleTimesInt")
operator fun Provider<Double>.times(other: Provider<Int>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("doubleTimesLong")
operator fun Provider<Double>.times(other: Provider<Long>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("doubleTimesFloat")
operator fun Provider<Double>.times(other: Provider<Float>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }

@JvmName("doubleTimesDouble")
operator fun Provider<Double>.times(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a * b }
//</editor-fold>

//<editor-fold desc="Division">
@JvmName("byteDivByte")
operator fun Provider<Byte>.div(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("byteDivShort")
operator fun Provider<Byte>.div(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("byteDivInt")
operator fun Provider<Byte>.div(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("byteDivLong")
operator fun Provider<Byte>.div(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("byteDivFloat")
operator fun Provider<Byte>.div(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("byteDivDouble")
operator fun Provider<Byte>.div(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("shortDivByte")
operator fun Provider<Short>.div(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("shortDivShort")
operator fun Provider<Short>.div(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("shortDivInt")
operator fun Provider<Short>.div(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("shortDivLong")
operator fun Provider<Short>.div(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("shortDivFloat")
operator fun Provider<Short>.div(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("shortDivDouble")
operator fun Provider<Short>.div(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("intDivByte")
operator fun Provider<Int>.div(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("intDivShort")
operator fun Provider<Int>.div(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("intDivInt")
operator fun Provider<Int>.div(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("intDivLong")
operator fun Provider<Int>.div(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("intDivFloat")
operator fun Provider<Int>.div(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("intDivDouble")
operator fun Provider<Int>.div(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("longDivByte")
operator fun Provider<Long>.div(other: Provider<Byte>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("longDivShort")
operator fun Provider<Long>.div(other: Provider<Short>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("longDivInt")
operator fun Provider<Long>.div(other: Provider<Int>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("longDivLong")
operator fun Provider<Long>.div(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("longDivFloat")
operator fun Provider<Long>.div(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("longDivDouble")
operator fun Provider<Long>.div(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("floatDivByte")
operator fun Provider<Float>.div(other: Provider<Byte>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("floatDivShort")
operator fun Provider<Float>.div(other: Provider<Short>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("floatDivInt")
operator fun Provider<Float>.div(other: Provider<Int>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("floatDivLong")
operator fun Provider<Float>.div(other: Provider<Long>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("floatDivFloat")
operator fun Provider<Float>.div(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("floatDivDouble")
operator fun Provider<Float>.div(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("doubleDivByte")
operator fun Provider<Double>.div(other: Provider<Byte>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("doubleDivShort")
operator fun Provider<Double>.div(other: Provider<Short>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("doubleDivInt")
operator fun Provider<Double>.div(other: Provider<Int>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("doubleDivLong")
operator fun Provider<Double>.div(other: Provider<Long>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("doubleDivFloat")
operator fun Provider<Double>.div(other: Provider<Float>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }

@JvmName("doubleDivDouble")
operator fun Provider<Double>.div(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a / b }
//</editor-fold>

//<editor-fold desc="Remainder">
@JvmName("byteRemByte")
operator fun Provider<Byte>.rem(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("byteRemShort")
operator fun Provider<Byte>.rem(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("byteRemInt")
operator fun Provider<Byte>.rem(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("byteRemLong")
operator fun Provider<Byte>.rem(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("byteRemFloat")
operator fun Provider<Byte>.rem(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("byteRemDouble")
operator fun Provider<Byte>.rem(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("shortRemByte")
operator fun Provider<Short>.rem(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("shortRemShort")
operator fun Provider<Short>.rem(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("shortRemInt")
operator fun Provider<Short>.rem(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("shortRemLong")
operator fun Provider<Short>.rem(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("shortRemFloat")
operator fun Provider<Short>.rem(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("shortRemDouble")
operator fun Provider<Short>.rem(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("intRemByte")
operator fun Provider<Int>.rem(other: Provider<Byte>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("intRemShort")
operator fun Provider<Int>.rem(other: Provider<Short>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("intRemInt")
operator fun Provider<Int>.rem(other: Provider<Int>): Provider<Int> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("intRemLong")
operator fun Provider<Int>.rem(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("intRemFloat")
operator fun Provider<Int>.rem(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("intRemDouble")
operator fun Provider<Int>.rem(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("longRemByte")
operator fun Provider<Long>.rem(other: Provider<Byte>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("longRemShort")
operator fun Provider<Long>.rem(other: Provider<Short>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("longRemInt")
operator fun Provider<Long>.rem(other: Provider<Int>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("longRemLong")
operator fun Provider<Long>.rem(other: Provider<Long>): Provider<Long> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("longRemFloat")
operator fun Provider<Long>.rem(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("longRemDouble")
operator fun Provider<Long>.rem(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("floatRemByte")
operator fun Provider<Float>.rem(other: Provider<Byte>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("floatRemShort")
operator fun Provider<Float>.rem(other: Provider<Short>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("floatRemInt")
operator fun Provider<Float>.rem(other: Provider<Int>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("floatRemLong")
operator fun Provider<Float>.rem(other: Provider<Long>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("floatRemFloat")
operator fun Provider<Float>.rem(other: Provider<Float>): Provider<Float> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("floatRemDouble")
operator fun Provider<Float>.rem(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("doubleRemByte")
operator fun Provider<Double>.rem(other: Provider<Byte>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("doubleRemShort")
operator fun Provider<Double>.rem(other: Provider<Short>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("doubleRemInt")
operator fun Provider<Double>.rem(other: Provider<Int>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("doubleRemLong")
operator fun Provider<Double>.rem(other: Provider<Long>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("doubleRemFloat")
operator fun Provider<Double>.rem(other: Provider<Float>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }

@JvmName("doubleRemDouble")
operator fun Provider<Double>.rem(other: Provider<Double>): Provider<Double> =
    combinedProvider(this, other) { a, b -> a % b }
//</editor-fold>

//<editor-fold desc="Unary minus">
@JvmName("byteUnaryMinus")
operator fun Provider<Byte>.unaryMinus(): Provider<Int> =
    map { -it }

@JvmName("shortUnaryMinus")
operator fun Provider<Short>.unaryMinus(): Provider<Int> =
    map { -it }

@JvmName("intUnaryMinus")
operator fun Provider<Int>.unaryMinus(): Provider<Int> =
    map { -it }

@JvmName("longUnaryMinus")
operator fun Provider<Long>.unaryMinus(): Provider<Long> =
    map { -it }

@JvmName("floatUnaryMinus")
operator fun Provider<Float>.unaryMinus(): Provider<Float> =
    map { -it }

@JvmName("doubleUnaryMinus")
operator fun Provider<Double>.unaryMinus(): Provider<Double> =
    map { -it }
//</editor-fold>

//<editor-fold desc="Increment">
@JvmName("byteInc")
operator fun Provider<Byte>.inc(): Provider<Byte> =
    map { (it + 1).toByte() }

@JvmName("shortInc")
operator fun Provider<Short>.inc(): Provider<Short> =
    map { (it + 1).toShort() }

@JvmName("intInc")
operator fun Provider<Int>.inc(): Provider<Int> =
    map { it + 1 }

@JvmName("longInc")
operator fun Provider<Long>.inc(): Provider<Long> =
    map { it + 1 }

@JvmName("floatInc")
operator fun Provider<Float>.inc(): Provider<Float> =
    map { it + 1 }

@JvmName("doubleInc")
operator fun Provider<Double>.inc(): Provider<Double> =
    map { it + 1 }
//</editor-fold>

//<editor-fold desc="Decrement">
@JvmName("byteDec")
operator fun Provider<Byte>.dec(): Provider<Byte> =
    map { (it - 1).toByte() }

@JvmName("shortDec")
operator fun Provider<Short>.dec(): Provider<Short> =
    map { (it - 1).toShort() }

@JvmName("intDec")
operator fun Provider<Int>.dec(): Provider<Int> =
    map { it - 1 }

@JvmName("longDec")
operator fun Provider<Long>.dec(): Provider<Long> =
    map { it - 1 }

@JvmName("floatDec")
operator fun Provider<Float>.dec(): Provider<Float> =
    map { it - 1 }

@JvmName("doubleDec")
operator fun Provider<Double>.dec(): Provider<Double> =
    map { it - 1 }
//</editor-fold>