package com.example.healax.todolist.controller;

import com.example.healax.exception.CommonResponse;
import com.example.healax.exception.TodoNotFoundException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.todolist.dto.TodoIdListDTO;
import com.example.healax.todolist.dto.TodoListDTO;
import com.example.healax.todolist.dto.TodoStatusDTO;
import com.example.healax.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<CommonResponse> addTodoList(@RequestBody TodoListDTO todoListDTO) {

        todoService.save(todoListDTO);

        CommonResponse response = new CommonResponse(
                "ToDo list에 정상적으로 해당 항목이 추가되었습니다.",
                201
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 해당 투두리스트 상태 변경
    @PostMapping("status")
    public ResponseEntity<?> updateTodoStatus(@RequestBody TodoStatusDTO todoStatusDTO) {

        todoService.updateTodoStatus(todoStatusDTO);

        CommonResponse response = new CommonResponse(
                "완료상태에 대한 수정이 정상적으로 완료되었습니다.",
                200
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 투두리스트 수정
    @PutMapping()
    public ResponseEntity<CommonResponse> updateTodoList(@RequestBody TodoListDTO todoListDTO) {

        todoService.updateTodoList(todoListDTO);

        CommonResponse response = new CommonResponse(
                "투두리스트 수정이 정상적으로 완료되었습니다.",
                200
        );
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // 투두리스트 하나 삭재
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteTodo(@PathVariable Long id) {

        todoService.delete(id);

        CommonResponse response = new CommonResponse(
                "투두리스트 1개가 정상적으로 삭제되었습니다.",
                204
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);

    }


    // 투두리스트 여러개 삭제
    @DeleteMapping("/del_list")
    public ResponseEntity<CommonResponse> deleteTodoList(@RequestBody List<TodoIdListDTO> id) {

        List<Long> ids = id.stream()
                    .map(TodoIdListDTO::getId)
                    .toList();

        todoService.todoListDeletes(ids);

        CommonResponse response = new CommonResponse(
                "투두리스트 여러개가 정상적으로 삭제되었습니다.",
                204
        );

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
