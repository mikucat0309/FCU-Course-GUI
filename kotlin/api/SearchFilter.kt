package api

import api.dto.*


class SearchFilter(apply: SearchFilter.() -> Unit) {
    init {
        apply(this)
    }

    var year: Int = 109
    var semester: Int = 2
    var code: Int? = null
        set(value) {
            if (value == null) return
            if (value < 1 || value > 9999) return
            field = value
        }
    var week: Int? = null
        set(value) {
            if (value == null) return
            if (value < 1 || value > 7) return
            field = value
        }
    var section: Int? = null
        set(value) {
            if (value == null) return
            if (value < 1 || value > 14) return
            field = value
        }
    var name: String? = null
    var teacher: String? = null
    var lang: Language = Language.CHINESE
    var subject: Subject? = null
    var description: String? = null

    fun toDTO(): SearchFilterDTO {
        return SearchFilterDTO(
            BaseOptions("cht", year, semester),
            TypeOptions(
                code = BasicTypeOption(code != null, code?.toString()),
                weekPeriod = WeekPeriodOption(
                    week != null,
                    if (week == null) "*" else week.toString(),
                    if (section == null) "*" else section.toString()
                ),
                course = BasicTypeOption(name != null, name),
                teacher = BasicTypeOption(teacher != null, teacher),
                useEnglish = BasicTypeOption(false),
                useLanguage = BasicTypeOption(true, lang.value),
                specificSubject = BasicTypeOption(
                    subject != null,
                    subject?.value ?: Subject.GENERAL.value
                ),
                courseDescription = BasicTypeOption(description != null, description)
            )
        )
    }

}

enum class Language(val value: String) {
    CHINESE("01"),
    ENGLISH("02"),
    JAPANESE("03"),
    GERMAN("04"),
    FRENCH("05"),
    SPANISH("06"),
    OTHER("07"),
    CHINESE_ENGLISH("08")
}

enum class Subject(val value: String) {
    GENERAL("1"),
    PE("2"),
    FRESHMAN_CHINESE("3")
}