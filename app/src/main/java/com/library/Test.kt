package com.library


import android.webkit.WebSettings.RenderPriority

fun main(){
    println("Hello, Test!")

    val taskFlow = TaskFlow()

    while (true) {
        taskFlow.run {
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> createTaskSafely()
                2 -> printTaskList()
                3 -> showCompletedTasks()
                4 -> markTask()
                5 -> showTasksByDescription()
                6 -> return
                else -> println("Wrong choice, try again.")
            }
        }
    }
}

class Task(
    val taskId: Int,
    val title: String,
    val description: String,
    val priority: Int,
    var isCompleted: Boolean
)

class TaskFlow {
    var taskIdCounter = 0
    val taskList by lazy {
//        println("Создан список задач")
        mutableListOf<Task>()
    }

    fun createTaskSafely(){
        runCatching {
            createdTask()
        }.onSuccess { task ->
            taskList.add(task)
        }.onFailure {
            println("Ошибка при добавлении задачи: $(it.message)")
        }
    }

    private fun createdTask(): Task {
        println("Enter the task name: ")
        val title = readlnOrNull()?.ifEmpty { null }
        require(title != null) {"Название не может быть пустым"}

        println("Enter a description: ")
        val description = readlnOrNull().orEmpty()

        println("Enter the priority of the task (0 - low, 1 - medium, 2 - high): ")
        val priority = readlnOrNull()?.toIntOrNull() ?: 0

        require(priority in 0 .. 2) {"Приоритет должен быть в диапозоне от 0 до 2"}

        val createdTask = Task(
            taskId = taskIdCounter++,
            title = title,
            description = description,
            priority = priority,
            isCompleted = false
        )

        return createdTask
    }

    fun printTaskList(){
        if (taskList.isEmpty()){
            println("The task list is empty.")
        } else {
            taskList.forEach { taskToPrint ->
                println(taskToPrint.formatToString())
            }
        }
    }

    fun showCompletedTasks(){
        taskList
            .asSequence()
            .filter { task ->
                task.isCompleted
            }.map { task ->
                task.formatToString()
            }.forEach { task ->
                println(task)
            }
    }

    fun showTasksByDescription() {
        println("Enter the task description: ")
        val descriptionFind = readlnOrNull().orEmpty()

        val predicate: (Task) -> Boolean = { task ->
            task.description.contains(descriptionFind)
        }

        for (task in taskList){
            if (predicate(task)){
                println(task.formatToString())
            }
        }
    }

    fun markTask() {
        println("Enter the task number:")
        val taskNumber = readlnOrNull()?.toIntOrNull() ?: -1

        val taskIndex = taskList.indexOfFirst { it.taskId == taskNumber }
        if (taskIndex == -1) {
            println("Task not found")
            return
        }

        taskList[taskIndex].isCompleted = taskList[taskIndex].isCompleted.not()
        println("The task status has been updated")
    }

    private fun Task.formatToString() = """
    Task(
        taskId=$taskId,
        title=$title,
        description=$description,
        priority=$priority,
        isCompleted=$isCompleted
    )
""".trimIndent()
}