package com.example.demo.todolist.controller;

import com.example.demo.exception.TodoNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.todolist.dto.TodoIdListDTO;
import com.example.demo.todolist.dto.TodoListDTO;
import com.example.demo.todolist.dto.TodoStatusDTO;
import com.example.demo.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    // 해당 유저 투두리스트 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getTodoList(@PathVariable String userId) {

        try {

            List<TodoListDTO> todoList = todoService.getTodoList(userId);

            return ResponseEntity.status(HttpStatus.OK).body(todoList);

        } catch (TodoNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 투두리스트 추가
    @PostMapping()
    public ResponseEntity<String> addTodoList(@RequestParam String userId, @RequestParam String todoTitle) {

        try {

            todoService.save(userId, todoTitle);

            return ResponseEntity.status(HttpStatus.CREATED).body("투두리스트 추가 성공");

        } catch (UserNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 해당 투두리스트 상태 변경
    @PostMapping("status")
    public ResponseEntity<?> updateTodoStatus(@RequestBody TodoStatusDTO todoStatusDTO) {

        try {

            todoService.updateTodoStatus(todoStatusDTO);

            return ResponseEntity.status(HttpStatus.OK).body("투두 상태 변경 완료");

        } catch (TodoNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 투두리스트 수정
    @PutMapping()
    public ResponseEntity<String> updateTodoList(@RequestParam Long id, @RequestParam String todoTitle) {

        try {

            todoService.updateTodoList(id, todoTitle);

            return ResponseEntity.status(HttpStatus.OK).body("투두리스트 " + id + " 수정 성공");

        } catch (TodoNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 투두리스트 하나 삭재
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {

        try {

            todoService.delete(id);

            return ResponseEntity.status(HttpStatus.OK).body("투두리스트 " + id + " 삭제 완료");

        } catch (TodoNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 투두리스트 여러개 삭제
    @DeleteMapping("/del_list")
    public ResponseEntity<String> deleteTodoList(@RequestBody List<TodoIdListDTO> id) {

        try {

            List<Long> ids = id.stream()
                    .map(TodoIdListDTO::getId)
                    .toList();

            todoService.todoListDeletes(ids);

            return ResponseEntity.status(HttpStatus.OK).body("투두리스트 " + ids + "를 삭제 했습니다.");

        } catch (TodoNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
