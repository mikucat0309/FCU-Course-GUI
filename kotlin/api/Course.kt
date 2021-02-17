package api

import api.dto.ClassesDTO
import api.dto.ClassesRawDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

data class Course(
    val name: String,
    val id: String,
    val code: Int,
    val teacher: String,
    val periods: List<Period>,
    val credit: Int,
    val type: CourseType,
    val opener: String,
    val openerID: String,
    val openNum: Int,
    val acceptNum: Int,
    val remark: String?,
)

fun fromClassesRawDTO(raw: ClassesRawDTO): List<Course> {
    val dto = Json.decodeFromString<ClassesDTO>(raw.d)
    return dto.run {
        items.map { courseDTO ->

            val periods1 = Regex("""\((.)\)(\d{2}) +(.+?) """)
                .findAll(courseDTO.scr_period)
                .map {
                    val (week, st, loc) = it.destructured
                    Period(chineseMapping[week]!!, IntRange(st.toInt(), st.toInt()), loc)
                }.toList()
            val periods2 = Regex("""\((.)\)(\d{2})-(\d{2}) +(.+?) """)
                .findAll(courseDTO.scr_period)
                .map {
                    val (week, st, ed, loc) = it.destructured
                    Period(chineseMapping[week]!!, IntRange(st.toInt(), ed.toInt()), loc)
                }.toList()
            Course(
                courseDTO.sub_name,
                courseDTO.sub_id3,
                courseDTO.scr_selcode.toInt(),
                courseDTO.scr_period.substringAfterLast(" "),
                periods1 + periods2,
                courseDTO.scr_credit.roundToInt(),
                CourseType.values().find { it.value == courseDTO.scj_scr_mso }!!,
                courseDTO.cls_name,
                courseDTO.cls_id,
                courseDTO.scr_precnt.roundToInt(),
                courseDTO.scr_acptcnt.roundToInt(),
                courseDTO.scr_remarks
            )
        }
    }
}

data class Period(
    val day: Int,
    val range: IntRange,
    val location: String
)

enum class CourseType(val value: String) {
    OPTIONAL("選修"),
    ESSENTIAL("必修")
}

val chineseMapping = mapOf(
    "一" to 1,
    "二" to 2,
    "三" to 3,
    "四" to 4,
    "五" to 5,
    "六" to 6,
    "日" to 7,
)

val chineseReverseMapping = mapOf(
    1 to "一",
    2 to "二",
    3 to "三",
    4 to "四",
    5 to "五",
    6 to "六",
    7 to "日",
)