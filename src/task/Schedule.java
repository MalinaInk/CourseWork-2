package task;

import java.time.LocalDate;
import java.util.*;

public class Schedule {
    private final Map<Integer, Task> tasks = new HashMap<>();
//добавляем задачу
    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }
//выдаем все заведенные задачи
    public Collection<Task> getAllTasks() {
        return this.tasks.values();
    }
//выдаем все задачи на введенную дату
    public Collection<Task> getTaskForDate(LocalDate date) {
        TreeSet<Task> taskForDate = new TreeSet<>();
        for (Task task : tasks.values()) {
            if (task.appearsIn(date)) {
                taskForDate.add(task);
            }
        }
        return taskForDate;
    }
    //удаляет задачу по id
    public void removeTask(int id) throws TaskNotFoundException {
        if (this.tasks.containsKey(id)) {
            this.tasks.remove(id);
        } else {
            throw new TaskNotFoundException();
        }
    }
}
