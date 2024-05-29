package com.example.myfirstapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstapplication.ui.theme.MyFirstApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFirstApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // A surface container using the 'background' color from the theme
                    MyTodoApp()
                }
            }
        }
    }
}
@Composable
fun MyTodoApp() {
    val todo = remember { mutableStateOf("") }
    val todoList = remember { mutableStateListOf<Todo>()}
    MyTodoAppContent(todo = todo, todoList = todoList)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTodoAppContent(
    todo: MutableState<String>,
    todoList: SnapshotStateList<Todo>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "MyToDo",
                    color = Color.White
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE) // トップバーの背景色
                )
            )
        }
    )   { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)) {
                TextField(
                    value = todo.value,
                    onValueChange = { text -> todo.value = text },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1f)
                )
                Button(onClick = {
                    todoList.add(Todo(text = todo.value, isCompleted = false))
                    todo.value = ""
                }) {
                    Text(text = "追加")
                }
            }
            todoList.forEachIndexed { index, item ->
                TodoItem(
                    todo = item,
                    deleteTodo = { todoList.removeAt(index)},
                    completeTodo = { todoList[index] = item.copy(isCompleted = it) }
                )
            }
        }
    }
}
@Composable
fun TodoItem(
    todo: Todo,
    deleteTodo: () -> Unit,
    completeTodo: (Boolean) -> Unit) {
    val fontColor =
        if (todo.isCompleted) {
            Color.Gray
        } else {
            Color.Black
        }
    val textDecoration =
        if (todo.isCompleted) {
            TextDecoration.LineThrough
        } else {
            null
        }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = todo.text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f),
            color = fontColor,
            textDecoration = textDecoration
        )
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = completeTodo,
            modifier = Modifier.padding(start = 4.dp)
        )
        IconButton(onClick = { deleteTodo() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription ="削除ボタン")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyFirstApplicationTheme {
        MyTodoAppContent(
            todo = remember { mutableStateOf("ToDoを入力") },
            todoList = remember {
                mutableStateListOf(
                    Todo(text = "長いTODOの場合はこのように適宜改行されるようになっている", isCompleted = false),
                    Todo(text = "完了した TODO", isCompleted = true),
                    Todo(text = "睡眠をとる", isCompleted = false)
                )
            }
        )
    }
}
