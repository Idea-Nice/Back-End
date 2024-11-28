package com.example.healax.todolist.service;

import com.example.healax.exception.TodoNotFoundException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.todolist.dto.TodoListDTO;
import com.example.healax.todolist.dto.TodoStatusDTO;
import com.example.healax.todolist.domain.Todo;
import com.example.healax.todolist.repository.TodoRepository;
import com.example.healax.user.domain.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    /*
     * 투두리스트 전체 조회
     * 리스트를 감싼 옵셔널로 반환해서 유저의 투두리스트가 있는지 판단
     * 있으면 DTO 리스트 만들어 for문 통에 추가 후 리턴
     * 없으면 예외처리 */
    public List<TodoListDTO> getTodoList(String userId) {

        Optional<List<Todo>> todoListOptional = todoRepository.findByUser_UserId(userId);

        if (todoListOptional.get().isEmpty()) {

            throw new TodoNotFoundException(userId + " 해당 유저의 투두리스트를 찾을 수 없습니다.");
        }

        List<Todo> todoList = todoListOptional.get();

        List<TodoListDTO> todoListDTOList = new ArrayList<>();

        for (Todo todo : todoList) {

            TodoListDTO todoListDTO = new TodoListDTO();

            todoListDTO.setId(todo.getId());
            todoListDTO.setTitle(todo.getTitle());
            todoListDTO.setCompleted(todo.isCompleted());

            todoListDTOList.add(todoListDTO);
        }

        return todoListDTOList;
    }

    /*

=======
    * 투두리스트 추가하기
    * 투두 객체 생성후 타이틀 저장
    * 유저 레포에 찾는 유저가 없으면 예외 발생 */
    public void save(TodoListDTO todoListDTO) {

        Optional<User> user = userRepository.findByUserId(todoListDTO.getUserId());

        if (user.isPresent()) {

            Todo todo = new Todo();

            todo.setUser(user.get());
            todo.setTitle(todoListDTO.getTitle());

            todoRepository.save(todo);

        } else {

            throw new UserNotFoundException(todoListDTO.getUserId() + " 유저를 찾을 수 없습니다.");
        }

    }

    /*
     * 투두리스트 상태 변경
     * 투두리스트를 id로 해당 투두리스트 조회
     * 투두리스트 있으면 상태 변경
     * 없으면 id 해당 투두리스트를 조회할 수 없음을 알림 */
    public void updateTodoStatus(TodoStatusDTO todoStatusDTO) {

        Optional<Todo> todoOptional = todoRepository.findById(todoStatusDTO.getId());

        if (todoOptional.isPresent()) {

            Todo todo = todoOptional.get();

            todo.setCompleted(todoStatusDTO.getCompleted());

            todoRepository.save(todo);

        } else {

            throw new TodoNotFoundException("해당 투두 " + todoStatusDTO.getId() +" 아이디를 찾을 수 없습니다.");
        }
    }

    /*

    * 투두리스트 수정
    * id로 검색후 존재하면 변경
    * 없으면 예외처리*/
    public void updateTodoList(TodoListDTO todoListDTO) {

        Optional<Todo> todoOptional = todoRepository.findById(todoListDTO.getId());

        if (todoOptional.isPresent()) {

            Todo todo = todoOptional.get();

            todo.setTitle(todoListDTO.getTitle());

            todoRepository.save(todo);

        } else {

            throw new TodoNotFoundException("해당 투두 " + todoListDTO.getId() + " 아이디를 찾을 수 없습니다.");
        }
    }

    /*
     * 투두리스트 한개 삭제
     * id로 검색한 투두리스트가 있으면 삭제
     * 없으면 예외처리 */
    public void delete(Long id) {

        Optional<Todo> todoOptional = todoRepository.findById(id);

        if (todoOptional.isPresent()) {

            todoRepository.delete(todoOptional.get());

        } else {

            throw new TodoNotFoundException("해당 투두 " + id +" 아이디를 찾을 수 없습니다.");
        }
    }


    /*
     * 투두리스트 여러개 삭제
     * 투두 id로 검색 후
     * 검색한 크기랑 id 크기랑 다르면 예외처리
     * 같으면 삭제 */
    public void todoListDeletes(List<Long> id) {

        List<Todo> todoLists = todoRepository.findAllById(id);

        if (todoLists.size() != id.size()) {

            throw new TodoNotFoundException("몇개의 투두리스트를 찾을 수 없습니다.");
        }

        todoRepository.deleteAll(todoLists);
    }
}
