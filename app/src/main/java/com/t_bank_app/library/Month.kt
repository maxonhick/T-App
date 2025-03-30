package com.t_bank_app.library

enum class Month {
    January {
        override fun toString(): String {
            return "Январь"
        }
    },
    February {
        override fun toString(): String {
            return "Февраль"
        }
    },
    March {
        override fun toString(): String {
            return "Март"
        }
    },
    April {
        override fun toString(): String {
            return "Апрель"
        }
    },
    May {
        override fun toString(): String {
            return "Май"
        }
    },
    June {
        override fun toString(): String {
            return "Июнь"
        }
    },
    July {
        override fun toString(): String {
            return "Июль"
        }
    },
    August {
        override fun toString(): String {
            return "Август"
        }
    },
    September {
        override fun toString(): String {
            return "Сентябрь"
        }
    },
    October {
        override fun toString(): String {
            return "Октябрь"
        }
    },
    November {
        override fun toString(): String {
            return "Ноябрь"
        }
    },
    December {
        override fun toString(): String {
            return "Декабрь"
        }
    }
}