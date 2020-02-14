package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern:String="HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if(this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time/ DAY
    val day2 = date.time/ DAY
    return day1 == day2
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND):Date{
    var time = this.time

    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR   -> value * HOUR
        TimeUnits.DAY    -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {

    var diff =  date.time - this.time

    var diff2:Double = diff.toDouble()
    if (diff < 0) diff2*=-1

    var number1:Double

    fun multiple(units: TimeUnits, one:String, two:String, five:String):String {

         number1 = when(units){
             TimeUnits.SECOND -> diff2 / SECOND
             TimeUnits.MINUTE -> diff2 / MINUTE
             TimeUnits.HOUR   -> diff2 / HOUR
             TimeUnits.DAY    -> diff2 / DAY
         }.toDouble()

        val number = number1.roundToInt()

        val text = if((number - number % 10) %100 != 10){
            when {
                number % 10 == 1 -> {one}
                number % 10 in 2..4 -> {two}
                else -> {five}
            }
        }else five
        return "$number $text"
    }

    return if(diff >= 0){
            when(diff){
                in 0..1*SECOND  -> "только что"
                in 1*SECOND..45*SECOND -> "несколько секунд назад"
                in 45*SECOND..75*SECOND -> "минуту назад"
                in 75*SECOND..45*MINUTE -> "${multiple(TimeUnits.MINUTE,"минута", "минуты", "минут" )} назад"
                in 45*MINUTE..75*MINUTE -> "час назад"
                in 75*MINUTE..22*HOUR -> "${multiple(TimeUnits.HOUR,"час", "часа", "часов" )} назад"
                in 22*HOUR..26*HOUR -> "день назад"
                in 26*HOUR..360*DAY -> "${multiple(TimeUnits.DAY,"день", "дня", "дней" )} назад"
                else -> "более года назад"
            }
        }else{
            diff*=-1
            when(diff){
                in 0..1*SECOND  -> "только что"
                in 1*SECOND..45*SECOND -> "через несколько секунд"
                in 45*SECOND..75*SECOND -> "через минуту"
                in 75*SECOND..45*MINUTE -> "через ${multiple(TimeUnits.MINUTE,"минута", "минуты", "минут" )}"
                in 45*MINUTE..75*MINUTE -> "через час"
                in 75*MINUTE..22*HOUR -> "через ${multiple(TimeUnits.HOUR,"час", "часа", "часов" )}"
                in 22*HOUR..26*HOUR -> "через день"
                in 26*HOUR..360*DAY -> "через ${multiple(TimeUnits.DAY,"день", "дня", "дней" )}"
                else -> "более чем через год"
            }
    }
}

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY;
    fun plural(count:Int): String{

        fun multiple(one:String, two:String, five:String):String {

            return if((count - count % 10) %100 != 10){
                when {
                    count % 10 == 1 -> {one}
                    count % 10 in 2..4 -> {two}
                    else -> {five}
                }
            }else five
        }

        val number = when(this){
            SECOND -> multiple("секунду", "секунды", "секунд")
            MINUTE -> multiple("минуту", "минуты", "минут")
            HOUR   -> multiple("час", "часа", "часов")
            DAY    -> multiple("день", "дня", "дней")
        }

        return "$count $number"
    }
}

