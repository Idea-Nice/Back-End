package com.example.healax.todolist.service;

import com.example.healax.todolist.entity.Todolist;
import com.example.healax.todolist.repository.TodolistRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodolistService {
    private final TodolistRepository todolistRepository;
    private final UserRepository userRepository;

    public TodolistService(TodolistRepository todolistRepository, UserRepository userRepository) {
        this.todolistRepository = todolistRepository;
        this.userRepository = userRepository;
    }

    public List<Todolist> getTodolistByUserId(String userId) {
        return todolistRepository.findByUser_UserId(userId);
    }

    public Optional<Todolist> getTodolistById(Long id) {
        return todolistRepository.findById(id);
    }

    public Todolist createTodolist(Todolist todolist, String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        user.ifPresent(todolist::setUser);
        return todolistRepository.save(todolist);
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

    public Todolist updateTodolist(Todolist todolist, String userId, Long id) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            Optional<Todolist> existingTodolist = todolistRepository.findById(id);
            if (existingTodolist.isPresent()) {
                Todolist updateTodo = existingTodolist.get();
                updateTodo.setTitle(todolist.getTitle());
                updateTodo.setCompleted(todolist.isCompleted());
                updateTodo.setUser(user.get());
                return todolistRepository.save(updateTodo);
            } else {
                throw new IllegalArgumentException("todolist 수정 오류 : 해당 투두리스트를 찾을 수 없습니다. id : " + id);
            }
        } else {
            throw new IllegalArgumentException("todolist 수정 오류 : 해당 유저 id를 찾을 수 없습니다. userId : " + userId);
        }
    }
    public void deleteTodolistById(Long id) {
        todolistRepository.deleteById(id);
    }
}
