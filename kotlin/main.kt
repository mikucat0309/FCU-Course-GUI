@file:Suppress("FunctionName")

import androidx.compose.desktop.Window
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import api.Course
import api.chineseReverseMapping

fun main() = Window(
    title = "FCU Course Search Plus",
    size = IntSize(1920, 1000),
) {
    val vm = MyViewModel()
    Table(vm)
}

@Composable
fun Table(viewModel: MyViewModel) {
    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            Week(viewModel.courses.value) { viewModel.onClick(it) }
            Spacer(Modifier.preferredHeight(16.dp))
            SearchPanel(viewModel)
            PopupWindow(viewModel.popState)
        }
    }
}

@Composable
fun SearchPanel(viewModel: MyViewModel) {
    Column {
        SearchField({ viewModel.onNameFieldChange(it) }, viewModel.nameField, "課程名稱")
        SearchField({ viewModel.onDayFieldChange(it) }, viewModel.dayField, "星期")
        SearchField({ viewModel.onSectionFieldChange(it) }, viewModel.sectionField, "節數")
        SearchField({ viewModel.onLocationFieldChange(it) }, viewModel.locationField, "地點")
        Spacer(Modifier.preferredHeight(16.dp))
        Row {
            Button({ viewModel.fetch() }) { Text("查詢") }
            Spacer(Modifier.preferredWidth(8.dp))
            Button({ viewModel.clear() }) { Text("清空") }
        }
    }
}

@Composable
fun SearchField(
    onValueChange: (String) -> Unit,
    state: MutableState<String>,
    fieldName: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            label = { Text(fieldName) },
            value = state.value,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1
        )
    }
}

@Composable
fun Week(courses: List<Course>, onClick: (Course) -> Unit) {
    ScrollableColumn(
        Modifier.padding(horizontal = 10.dp)
    ) {
        HeaderView()
        for (section in 1..14) {
            Section(section, courses.filter { c -> c.periods.flatMap { it.range }.contains(section) }, onClick)
        }
    }
}


@Composable
fun Section(index: Int, courses: List<Course>, onClick: (Course) -> Unit) {
    Row(
        modifier = Modifier.border(1.dp, Color.Black),
        horizontalArrangement = Arrangement.Center
    ) {
        CustomView(index, 50.dp, 100.dp)
        for (day in 1..7) {
            val overlapping = courses.filter { c -> c.periods.map { it.day }.contains(day) }
            ClassView(overlapping, onClick)
        }
    }
}

@Composable
fun HeaderView() {
    Row(
        modifier = Modifier.border(1.dp, Color.Black),
        horizontalArrangement = Arrangement.Center
    ) {
        CustomView(null, 50.dp, 50.dp)
        for (day in 1..7) {
            CustomView(day, 150.dp, 50.dp)
        }
    }
}

@Composable
fun CustomView(index: Int?, w: Dp, h: Dp) {
    Column(
        modifier = Modifier
            .preferredSize(w, h)
            .border(1.dp, Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                index?.toString() ?: "",
                style = typography.h6,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ClassView(courses: List<Course>, onClick: (Course) -> Unit) {
    Column(
        modifier = Modifier
            .preferredSize(150.dp, 100.dp)
            .border(1.dp, Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val content = courses.map { it.name.trim() }
        Column {
            courses.subList(0, courses.size.coerceAtMost(3))
                .forEach { c ->
                    Text(
                        c.name,
                        style = typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { onClick(c) }
                    )
                }
            if (courses.size > 3)
                Text(
                    "......",
                    style = typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}

@Composable
fun PopupWindow(popState: MutableState<Course?>) {
    val c = popState.value ?: return
    AlertDialog(
        shape = MaterialTheme.shapes.large,
        onDismissRequest = { popState.value = null },
        title = { Text(text = c.name) },
        text = {
            Text(
                text = "課程編號： ${c.id}\n" +
                        "選課代號： ${c.code}\n" +
                        "教師： ${c.teacher}\n" +
                        "學分： ${c.credit}\n" +
                        "已收/開放名額： ${c.acceptNum}/${c.openNum}\n" +
                        "課程類型： ${c.type.value}\n" +
                        "開課班級： ${c.opener}\n" +
                        "上課時段與地點：\n" +
                        c.periods.joinToString("\n") {
                            "    星期${chineseReverseMapping[it.day]} 第${it.range.first}~${it.range.last}節：${it.location}"
                        },
            )
        },
        confirmButton = {
            Button(onClick = { popState.value = null }) {
                Text("Close")
            }
        }
    )
}
