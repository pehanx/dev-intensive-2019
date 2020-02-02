package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.isValid


class Bender(var status:Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion():String =  when (question){
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer:String):Pair<String, Triple<Int, Int, Int>> {
//        return if (question == Question.IDLE) {
//            question.question to status.color
//        } else {
//            if (answer.isValid(question)) {
//                if (question.answer.contains(answer)) {
//                    question = question.nextQuestion()
//                    if (question == Question.IDLE) "${question.errorMessage}\n${question.question}" to status.color
//                    else "Отлично - это правильный ответ!\n${question.question}" to status.color
//                } else {
//                    status = status.nextStatus()
//                    "Это неправильный ответ!\n${question.question}" to status.color
//                }
//            } else{
//                "${question.errorMessage}\n${question.question}" to status.color
//            }
//        }

        return if (question.answer.contains(answer)) {
            question = question.nextQuestion()
            "Отлично - это правильный ответ!\n${question.question}" to status.color
        } else {
            status = status.nextStatus()
            "Это не правильный ответ!\n${question.question}" to status.color
        }

    }


    enum class Status(val color:Triple<Int, Int, Int>){
        NORMAL(Triple(255,255,255)),
        WARNING(Triple(255,120,0)),
        DANGER(Triple(255,60,60)),
        CRITICAL(Triple(255,255,0));

        fun nextStatus():Status{
            return if(this.ordinal<values().lastIndex){
                values()[this.ordinal +1]
            }else{
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>, val errorMessage:String){
        NAME("Как меня зовут?", listOf("Бендер","bender"), "Имя должно начинаться с заглавной буквы") {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик","bender"), "Профессия должна начинаться со строчной буквы"){
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл","дерево","metal","iron", "wood"), "Материал не должен содержать цифр"){
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993"), "Год моего рождения должен содержать только цифры"){
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057"), "Серийный номер содержит только цифры, и их 7"){
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf(), "Отлично - ты справился"){
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion():Question
    }
}


