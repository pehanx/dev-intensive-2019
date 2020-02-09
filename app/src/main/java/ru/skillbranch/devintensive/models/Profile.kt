package ru.skillbranch.devintensive.models

data class Profile(
    val firstName:String,
    val lastName:String,
    val about:String,
    val repository:String,
    val rating:Int = 0,
    val respect:Int = 0
) {

    val nickName:String = nickName()
    val rank:String = "Junior Android Developer"

    fun toMap():Map<String, Any> = mapOf(
        "nickName" to nickName,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )

    fun nickName(divider:String = "_"):String {
        val payload = "$firstName $lastName"
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
}