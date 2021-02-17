import androidx.compose.runtime.mutableStateOf
import api.Course
import api.HttpApi
import api.SearchFilter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyViewModel {
    val nameField = mutableStateOf("")
    val dayField = mutableStateOf("")
    val sectionField = mutableStateOf("")
    val locationField = mutableStateOf("")
    val courses = mutableStateOf(emptyList<Course>())
    val popState = mutableStateOf<Course?>(null)
    private val api = HttpApi()

    fun onNameFieldChange(newValue: String) {
        nameField.value = newValue
    }

    fun onDayFieldChange(newValue: String) {
        dayField.value = newValue
    }

    fun onSectionFieldChange(newValue: String) {
        sectionField.value = newValue
    }

    fun onLocationFieldChange(newValue: String) {
        locationField.value = newValue
    }

    fun onClick(c: Course) {
        popState.value = c
    }

    fun fetch() {
        val filter = SearchFilter {
            name = nameField.value
            week = dayField.value.toIntOrNull()
            section = sectionField.value.toIntOrNull()
        }
        GlobalScope.launch {
            val result = api.search(filter)
            val location = locationField.value
            courses.value = result.filter { c ->
                c.periods
                    .map { it.location }
                    .forEach {
                        if (location in it) return@filter true
                    }
                return@filter false
            }
        }
    }

    fun clear() {
        nameField.value = ""
        dayField.value = ""
        sectionField.value = ""
        locationField.value = ""
    }
}
