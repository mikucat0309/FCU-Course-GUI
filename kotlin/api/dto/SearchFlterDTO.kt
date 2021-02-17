package api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilterDTO(
    val baseOptions: BaseOptions,
    val typeOptions: TypeOptions,
) : DTO

@Serializable
data class BaseOptions(
    val lang: String,
    val year: Int,
    val sms: Int,
)

@Serializable
data class TypeOptions(
    val code: BasicTypeOption,
    val weekPeriod: WeekPeriodOption,
    val course: BasicTypeOption,
    val teacher: BasicTypeOption,
    val useEnglish: BasicTypeOption,
    val useLanguage: BasicTypeOption,
    val specificSubject: BasicTypeOption,
    val courseDescription: BasicTypeOption,
)

@Serializable
data class BasicTypeOption(
    val enabled: Boolean,
    val value: String? = null,
)

@Serializable
data class WeekPeriodOption(
    val enabled: Boolean,
    val week: String?,
    val period: String?,
)
