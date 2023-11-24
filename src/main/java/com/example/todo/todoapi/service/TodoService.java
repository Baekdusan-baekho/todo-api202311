package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoListResponseDTO create(final TodoCreateRequestDTO requestDTO )
            throws RuntimeException{
        todoRepository.save(requestDTO.toEntity());
        log.info("할 일 저장 완료! 제목: {}", requestDTO.getTitle());


        return retrieve();
    }

    public TodoListResponseDTO retrieve() {
        // 조회
        List<Todo> entityList = todoRepository.findAll();

        // 글 목록 가져오기  메서드로 추출 엔터티를 디티오로 변환
        List<TodoDetailResponseDTO> dtoList
                = entityList.stream()
                /*.map(todo -> new TodoDetailResponseDTO(todo))*/
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());


        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }


}







