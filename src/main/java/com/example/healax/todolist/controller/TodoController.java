package com.example.healax.todolist.controller;

import com.example.healax.exception.TodoNotFoundException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.todolist.dto.TodoIdListDTO;
import com.example.healax.todolist.dto.TodoListDTO;
import com.example.healax.todolist.dto.TodoStatusDTO;
import com.example.healax.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
@CrossOrigin("http://localhost:3000/")
public class TodoController {

    private final TodoService todoService;

    // 해당 유저 투두리스트 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getTodoList(@PathVariable String userId) {

        List<TodoListDTO> todoList = todoService.getTodoList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(todoList);

    }

    // 투두리스트 추가
    @PostMapping()
    public ResponseEntity<String> addTodoList(@RequestBody TodoListDTO todoListDTO) {

        todoService.save(todoListDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("투두리스트 추가 성공");
    }

    // 해당 투두리스트 상태 변경
    @PostMapping("status")
    public ResponseEntity<?> updateTodoStatus(@RequestBody TodoStatusDTO todoStatusDTO) {

        todoService.updateTodoStatus(todoStatusDTO);

        return ResponseEntity.status(HttpStatus.OK).body("투두 상태 변경 완료");
    }

    // 투두리스트 수정
    @PutMapping()
    public ResponseEntity<String> updateTodoList(@RequestBody TodoListDTO todoListDTO) {


        todoService.updateTodoList(todoListDTO);

        return ResponseEntity.status(HttpStatus.OK).body("투두리스트 " + todoListDTO.getId() + " 수정 성공");

    }

    // 투두리스트 하나 삭재
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {


        todoService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body("투두리스트 " + id + " 삭제 완료");

    }


    // 투두리스트 여러개 삭제
    @DeleteMapping("/del_list")
    public ResponseEntity<String> deleteTodoList(@RequestBody List<TodoIdListDTO> id) {

        List<Long> ids = id.stream()
                    .map(TodoIdListDTO::getId)
                    .toList();

        todoService.todoListDeletes(ids);

        return ResponseEntity.status(HttpStatus.OK).body("투두리스트 " + ids + "를 삭제 했습니다.");
    }
}
