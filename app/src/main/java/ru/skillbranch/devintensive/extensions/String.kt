package ru.skillbranch.devintensive.extensions

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