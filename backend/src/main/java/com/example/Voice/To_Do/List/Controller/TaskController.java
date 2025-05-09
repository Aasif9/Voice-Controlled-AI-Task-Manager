package com.example.Voice.To_Do.List.Controller;

import com.example.Voice.To_Do.List.Model.Task;
import com.example.Voice.To_Do.List.Model.User;
import com.example.Voice.To_Do.List.Repository.UserRepository;
import com.example.Voice.To_Do.List.Service.NotificationService;
import com.example.Voice.To_Do.List.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> getTasksByDateRange(@RequestParam(required = false) Long userId, @RequestParam(required = false) Integer days){
        List<Task> tasks = taskService.getTasksByDateRange(userId, days);
        return ResponseEntity.ok(tasks);
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        Task createdTask = taskService.createTask(task);

        if (createdTask != null && task.getUserId() != null) {
            Optional<User> user = userRepository.findById(task.getUserId());
            user.ifPresent(userData -> notificationService.sendTaskNotification(task, userData, false));
        }

        return ResponseEntity.ok(createdTask);
    }
//    @PostMapping
//    public ResponseEntity<Task> createTask(@RequestBody Task task){
//        Task createdTask = taskService.createTask(task.getOperation(), task.getTask(), task.getUrgency(), task.getDatetime(), task.getUserId());
//
//        if (createdTask != null && task.getUserId() != null) {
//            Optional<User> user = userRepository.findById(task.getUserId());
//            user.ifPresent(userData -> notificationService.sendTaskNotification(task, userData, false));
//        }
//
//        return ResponseEntity.ok(createdTask);
//    }

//    @GetMapping
//    public ResponseEntity<?> getTasks(@PathVariable Long userId){
//        List<Task> tasks = taskService.getTaskByUserId(userId);
//        return ResponseEntity.ok(tasks);
//    }
//    private static boolean isABoolean(Task task){
//        return task.getTask() == null || task.getTask().isBlank();
//    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id){

        Optional<Task> task = taskService.getTask(id);
        if(task.isPresent()){
            return ResponseEntity.ok(task.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
//        System.out.println("Returning Task");

//        return ResponseEntity.of(taskResponse);
    }
    @GetMapping
    public ResponseEntity<Iterable<Task>> getAllTasks(){

        Iterable<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
//        List<Task> taskResponse = taskService.getTask(); //
//        return ResponseEntity.of(Optional.of(taskResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task){
            Task updatedTask = taskService.updateTask(id, task.getOperation(), task.getTask(), task.getUrgency(), task.getDatetime().toString());
            if (updatedTask != null){
                if (task.getUserId() != null) {
                    Optional<User> user = userRepository.findById(task.getUserId());
                    user.ifPresent(userData -> notificationService.sendTaskNotification(task, userData, true));
                }
                return ResponseEntity.ok(updatedTask);
            }
            else {
                return ResponseEntity.notFound().build();
            }
//        Optional<Task> taskResponse = taskService.getTask(id);
//
//        System.out.println("Returning Task");
//
//        return ResponseEntity.of(taskResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){

        boolean deleted = taskService.deleteTask(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
