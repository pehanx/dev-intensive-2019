package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.Bender

fun String.truncate(countSymbols: Int = 16):String{

    return if(this.trim().length >= countSymbols){
        "${this.trim().substring(0, countSymbols).trim()}..."
    }else{
        this.trim()
    }
}

fun String.stripHtml():String{

    var result = Regex("""<[^>]*>""").replace(this, "")
               result = Regex("[&'\"]").replace(result, "")
               result = Regex("\\s+").replace(result, " ")
               result = result.trimStart()
               result = result.trim()

    return result
}

fun String.isValid(question: Bender.Question): Boolean {
    if(!this.isBlank()){
        return when (question) {
            Bender.Question.NAME -> {
                val firstChar = this[0]
                return firstChar.isUpperCase()
            }
            Bender.Question.PROFESSION -> {
                val firstChar = this[0]
                return firstChar.isLowerCase()
            }
            Bender.Question.MATERIAL -> {
                return !Regex("""\d+""").matches(this)
            }
            Bender.Question.BDAY -> {
                return Regex("""\d+""").matches(this)
            }
            Bender.Question.SERIAL -> {
                return if(this.length == 7){
                    Regex("""\d+""").matches(this)
                } else false
            }
            else -> true
        }
    }else return false
}