package ru.skillbranch.devintensive.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ru.skillbranch.devintensive.models.Bender

object Utils {
    fun parseFullName(fullName:String?):Pair<String?, String?> {

        val parts:List<String>? = fullName?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName= parts?.getOrNull(1)
//        return Pair(firstName, lastName)
        return if (fullName.isNullOrBlank()) null to null
        else firstName to lastName

    }

    fun transliteration(payload: String, divider:String = " "):String {

        val pattern = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ "

        return payload.replace(
                Regex("[$pattern]")) {
                when (it.value) {
                    "а"->"a"
                    "б"->"b"
                    "в"->"v"
                    "г"->"g"
                    "д"->"d"
                    "е"->"e"
                    "ё"->"e"
                    "ж"->"zh"
                    "з"->"z"
                    "и"->"i"
                    "й"->"i"
                    "к"->"k"
                    "л"->"l"
                    "м"->"m"
                    "н"->"n"
                    "о"->"o"
                    "п"->"p"
                    "р"->"r"
                    "с"->"s"
                    "т"->"t"
                    "у"->"u"
                    "ф"->"f"
                    "х"->"h"
                    "ц"->"c"
                    "ч"->"ch"
                    "ш"->"sh"
                    "щ"->"sh'"
                    "ъ"->""
                    "ы"->"i"
                    "ь"->""
                    "э"->"e"
                    "ю"->"yu"
                    "я"->"ya"
                    "А"->"A"
                    "Б"->"B"
                    "В"->"V"
                    "Г"->"G"
                    "Д"->"D"
                    "Е"->"E"
                    "Ё"->"E"
                    "Ж"->"Zh"
                    "З"->"Z"
                    "И"->"I"
                    "Й"->"I"
                    "К"->"K"
                    "Л"->"L"
                    "М"->"M"
                    "Н"->"N"
                    "О"->"O"
                    "П"->"P"
                    "Р"->"R"
                    "С"->"S"
                    "Т"->"T"
                    "У"->"U"
                    "Ф"->"F"
                    "Х"->"H"
                    "Ц"->"C"
                    "Ч"->"Ch"
                    "Ш"->"Sh"
                    "Щ"->"Sh'"
                    "Ъ"->""
                    "Ы"->"I"
                    "Ь"->""
                    "Э"->"E"
                    "Ю"->"Yu"
                    "Я"->"Ya"
                    " "->divider
                    else -> it.value
                }
            }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val firstInitial =
            if (!firstName.isNullOrBlank()) firstName[0].toUpperCase() else null

        val secondInitial =
            if (!lastName.isNullOrBlank()) lastName[0].toUpperCase() else null

        return if (firstInitial == null && secondInitial == null) {
            null
        } else if (firstInitial != null && secondInitial != null) {
            "$firstInitial$secondInitial"
        } else if(firstInitial == null){
            "$secondInitial"
        }else {
            "$firstInitial"
        }
    }

}