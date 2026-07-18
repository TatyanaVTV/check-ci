package ru.vtvhw.ci.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.vtvhw.ci.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping
    public List<Task> getAllTasks() {
        log.info("Get all tasks, count: {}", tasks.size());
        return tasks;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        task.setId(idGenerator.getAndIncrement());
        tasks.add(task);
        log.info("Created task: {}", task);
        return task;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        tasks.removeIf(t -> t.getId().equals(id));
        log.info("Deleted task with id: {}", id);
    }
}
