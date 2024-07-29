package com.example.healax.todolist.controller;

import com.example.healax.todolist.entity.Todolist;
import com.example.healax.todolist.service.TodolistService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/todo")
public class TodolistController {

    private final TodolistService todolistService;

    public TodolistController(TodolistService todolistService) {
        this.todolistService = todolistService;
    }

    // userId로 해당 유저 투두리스트 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Todolist>> getTodolistsByUserId(@PathVariable String userId) {
        List<Todolist> todolists = todolistService.getTodolistByUserId(userId);
        return ResponseEntity.ok(todolists);
    }

    // id(todolist의 id) 로 해당 투두리스트 요소 한개 조회
    @GetMapping("/getone/{id}")
    public ResponseEntity<Todolist> getTodolistById(@PathVariable Long id) {
        Optional<Todolist> todolist = todolistService.getTodolistById(id);
        return todolist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    // todolist 생성. (userId는 url로, id는 자동생성, 나머지 정보는 받는걸로)
    @PostMapping("/create/{userId}")
    public ResponseEntity<Todolist> createTodolist(@RequestBody Todolist todolist, @PathVariable String userId) {
        Todolist createTodo = todolistService.createTodolist(todolist, userId);
        return ResponseEntity.ok(createTodo);
    }

    // 완료 상태 바꾸기. //여기 id는 투두리스트 id
    @PostMapping("/toggle/{userId}/{id}")
    public ResponseEntity<Todolist> toggleCompletionStatus(@PathVariable String userId, @PathVariable Long id) {
        Todolist updatedTodo = todolistService.toggleCompletionStatus(id, userId);
        return ResponseEntity.ok(updatedTodo);
    }

    // todolist 수정. (todolist id와 userId로 수정)
    @PostMapping("/modify/{userId}/{id}")
    public ResponseEntity<Todolist> updateTodolist(@RequestBody Todolist todolist, @PathVariable String userId, @PathVariable Long id) {
        Todolist updateTodo = todolistService.updateTodolist(todolist, userId, id);
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
