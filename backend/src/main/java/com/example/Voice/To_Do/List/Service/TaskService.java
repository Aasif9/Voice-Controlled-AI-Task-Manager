package com.example.Voice.To_Do.List.Service;

import com.example.Voice.To_Do.List.Model.Task;
import com.example.Voice.To_Do.List.DAO.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

//    @Autowired
//    private MyAnalyzer myAnalyzer;

    public Task createTask(Task task){
        Task newTask = new Task();
        newTask.setUserId(task.getUserId());
//        String processedMessage = myAnalyzer.stem(task);
        newTask.setOperation(task.getOperation());
        newTask.setTask(task.getTask());
        newTask.setUrgency(task.getUrgency());
        newTask.setDatetime(task.getDatetime());
        return taskRepository.save(newTask);
    }
    public Task updateTask(Long id, String operation, String task, String urgency, String datetime) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task taskToUpdate = existingTask.get();
            taskToUpdate.setOperation(operation);
            taskToUpdate.setTask(task);
            taskToUpdate.setUrgency(urgency);
            taskToUpdate.setDatetime(datetime);
            return taskRepository.save(taskToUpdate);
        }
        return null;
    }

//    public Task createTask(String operation, String task, String urgency, String datetime, Long userId){
//        Task newTask = new Task();
//        String processedMessage = myAnalyzer.stem(task);
//        newTask.setOperation(operation);
//        newTask.setTask(processedMessage);
//        newTask.setUrgency(urgency);
//        newTask.setDatetime(datetime);
//        newTask.setUserId(userId);
//        return taskRepository.save(newTask);
//    }

    public Optional<Task> getTask(Long id){
        return taskRepository.findById(id);
    }
    public Iterable<Task> getAllTasks(){ //
        return taskRepository.findAll();
    }
    public boolean deleteTask(Long id){
        if(taskRepository.existsById(id)){
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Task> getTasksByDateRange(Long userId, Integer days){
        List<Task> tasks;
        if(userId != null){
            tasks = taskRepository.findByUserId(userId);
        }else {
            tasks = (List<Task>) taskRepository.findAll();
        }

        if(days == null || days <= 0){
            return tasks;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(days);
        return tasks.stream().filter(task ->{
            try{
                LocalDateTime taskData =parseDateTime(task.getDatetime());
                return taskData != null && !taskData.isBefore(now) &&
                        !taskData.isAfter(endDate);
            }catch (Exception e){
                return false; // skip tasks with invalid dates
            }
        }).collect(Collectors.toList());
    }

    private LocalDateTime parseDateTime (String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception exception) {
            try {
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception exception1) {
                throw new IllegalArgumentException("Invalid date time format: " + dateTime);
            }
        }
    }

//    public List<Task> getTaskByUserId(Long userId){ // class 10 : added
//        if(taskRepository.existsByUserId(userId)){
//            Optional<List<Task>> tasks = taskRepository.findAllByUserId(userId);
//            if(tasks.isPresent()){
//                return tasks.get();
//            }
//        }
//        return null;
//    }

//    public Optional<Task> findByTaskName(String taskName){
//
//    }
}

//class 11 :


