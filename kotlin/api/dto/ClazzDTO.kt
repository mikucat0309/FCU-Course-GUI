package api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClassesRawDTO(
    val d: String
)

@Serializable
data class ClassesDTO(
    val message: String?,
    val total: Int,
    val items: List<ClazzDTO>
)

@Serializable
data class ClazzDTO(
    val scr_selcode: String = "2911",
    val sub_id3: String = "GHUC205",
    val sub_name: String = "日本歷史與文化",
    val scr_credit: Double = 2.0,
    val scj_scr_mso: String = "選修",
    val scr_examid: String = "否",
    val scr_examfn: String = "否",
    val scr_exambf: String = "否",
    val cls_name: String = "通識－人文(H)",
    val scr_period: String = "(一)01-02 人703 黃煇慶",
    val scr_precnt: Double = 78.0,
    val scr_acptcnt: Double = 0.0,
    val scr_remarks: String? = "是否列入畢業學分請至各系確認。",
    val unt_ls: Int = 8888,
    val cls_id: String = "XC01129",
    val sub_id: String = "GH042",
    val scr_dup: String = "003",
) : DTO