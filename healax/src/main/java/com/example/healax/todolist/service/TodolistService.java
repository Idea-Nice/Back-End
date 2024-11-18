package com.example.healax.todolist.service;

import com.example.healax.todolist.dto.TodolistDTO;
import com.example.healax.todolist.entity.Todolist;
import com.example.healax.todolist.repository.TodolistRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodolistService {
    private final TodolistRepository todolistRepository;
    private final UserRepository userRepository;

    public TodolistService(TodolistRepository todolistRepository, UserRepository userRepository) {
        this.todolistRepository = todolistRepository;
        this.userRepository = userRepository;
    }

    public List<TodolistDTO> getTodolistByUserId(String userId) {
        List<Todolist> todolists = todolistRepository.findByUser_UserId(userId);
        return todolists.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TodolistDTO> getTodolistById(Long id) {
        return todolistRepository.findById(id).map(this::toDTO);
    }

    public Todolist createTodolist(Todolist todolist, String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        user.ifPresent(todolist::setUser);
        return todolistRepository.save(todolist);
    }

    // Todolist를 DTO로 변환
    public TodolistDTO toDTO (Todolist todolist) {
        TodolistDTO dto = new TodolistDTO();
        dto.setId(todolist.getId());
        dto.setTitle(todolist.getTitle());
        dto.setCompleted(todolist.isCompleted());
        dto.setUserId(todolist.getUser().getUserId());
        return dto;
    }

    // 완료 상태변경 (완료 -> 미완료, 미완료 -> 완료)
    public Todolist toggleCompletionStatus(Long id, String userId) {
        Optional<Todolist> todolistOptional = todolistRepository.findById(id);
        if (todolistOptional.isPresent()) {
            Todolist todolist = todolistOptional.get();
            if (todolist.getUser().getUserId().equals(userId)) {
                // 완료 상태 토글
                todolist.setCompleted(!todolist.isCompleted());
                return todolistRepository.save(todolist);
            } else {
                throw new IllegalArgumentException("투두리스트 수정 오류: 해당 유저에게 권한이 없습니다. userId: " + userId);
            }
        } else {
            throw new IllegalArgumentException("투두리스트 수정 오류: 해당 투두리스트를 찾을 수 없습니다. id: " + id);
        }
    }

    public Todolist updateTodolist(TodolistDTO todolistDTO) {
        Optional<User> user = userRepository.findByUserId(todolistDTO.getUserId());
        if (user.isPresent()) {
            Optional<Todolist> existingTodolist = todolistRepository.findById(todolistDTO.getId());
            if (existingTodolist.isPresent()) {
                Todolist updateTodo = existingTodolist.get();
                updateTodo.setTitle(todolistDTO.getTitle());
                updateTodo.setCompleted(todolistDTO.isCompleted());
                updateTodo.setUser(user.get());
                return todolistRepository.save(updateTodo);
            } else {
                throw new IllegalArgumentException("todolist 수정 오류 : 해당 투두리스트를 찾을 수 없습니다. id : " + todolistDTO.getId());
            }
        } else {
            throw new IllegalArgumentException("todolist 수정 오류 : 해당 유저 id를 찾을 수 없습니다. userId : " + todolistDTO.getUserId());
        }
    }
    public void deleteTodolistById(Long id) {
        todolistRepository.deleteById(id);
    }

    // 여러 todolist 항목 삭제 (todolist id 목록으로 삭제)
    public void deleteManyTodolists(String userId, List<Long> ids) {
        List<Todolist> todolists = todolistRepository.findAllById(ids);
        todolists.stream()
                .filter(todolist -> todolist.getUser().getUserId().equals(userId))
                .forEach(todolistRepository::delete);
    }
}
