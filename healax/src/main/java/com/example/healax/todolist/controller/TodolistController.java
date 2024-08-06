package com.example.healax.todolist.controller;

import com.example.healax.todolist.dto.TodolistDTO;
import com.example.healax.todolist.entity.Todolist;
import com.example.healax.todolist.service.TodolistService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/todo")
@CrossOrigin(origins = "http://43.203.68.91:80/", allowedHeaders = "*")
public class TodolistController {

    private final TodolistService todolistService;

    public TodolistController(TodolistService todolistService) {
        this.todolistService = todolistService;
    }

    // userId로 해당 유저 투두리스트 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<TodolistDTO>> getTodolistsByUserId(@PathVariable String userId) {
        List<TodolistDTO> todolists = todolistService.getTodolistByUserId(userId);
        return ResponseEntity.ok(todolists);
    }

    // id(todolist의 id) 로 해당 투두리스트 요소 한개 조회
    @GetMapping("/getone/{id}")
    public ResponseEntity<TodolistDTO> getTodolistById(@PathVariable Long id) {
        Optional<TodolistDTO> todolist = todolistService.getTodolistById(id);
        return todolist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    // todolist 생성. (userId는 url로, id는 자동생성, 나머지 정보는 받는걸로)
    @PostMapping("/create/{userId}")
    public ResponseEntity<TodolistDTO> createTodolist(@RequestBody Todolist todolist, @PathVariable String userId) {
        Todolist createTodo = todolistService.createTodolist(todolist, userId);
        TodolistDTO dto = todolistService.toDTO(createTodo);
        return ResponseEntity.ok(dto);
    }

    // 완료 상태 바꾸기. //여기 id는 투두리스트 id
    @PostMapping("/toggle/{userId}/{id}")
    public ResponseEntity<Todolist> toggleCompletionStatus(@PathVariable String userId, @PathVariable Long id) {
        Todolist updatedTodo = todolistService.toggleCompletionStatus(id, userId);
        return ResponseEntity.ok(updatedTodo);
    }

    // todolist 수정. (todolist id와 userId로 수정)
    @PostMapping("/modify")
    public ResponseEntity<Todolist> updateTodolist(@RequestBody TodolistDTO todolistDTO) {
        Todolist updateTodo = todolistService.updateTodolist(todolistDTO);
        return ResponseEntity.ok(updateTodo);
    }

    // todolist 삭제 (todolist id로 삭제)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodolistById(@PathVariable Long id) {
        todolistService.deleteTodolistById(id);
        return ResponseEntity.noContent().build();
    }

    // 여러개 한번에 삭제. (json으로 todolist Id 여러개 받음)
    @DeleteMapping("/deleteMany/{userId}")
    public ResponseEntity<Void> deleteManyTodolists(@PathVariable String userId, @RequestBody List<Long> ids) {
        todolistService.deleteManyTodolists(userId, ids);
        return ResponseEntity.noContent().build();
    }
}
