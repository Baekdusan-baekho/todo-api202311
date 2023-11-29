package com.example.todo.todoapi.api;

import com.example.todo.auth.TokenUserInfo;
import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@CrossOrigin // 리액트와 연결하는 아노테이션 데이터를 브라우저에 보내기 성공
public class TodoController {

    private final TodoService todoService;

    // 할 일 등록 요청
    @PostMapping
    public ResponseEntity<?> createTodo(
            // @Validated =  @NotBlank
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody TodoCreateRequestDTO requestDTO, // 타이틀이 올 것이다 json의 형태로 @RequestBody
            BindingResult result // 요청 처리에 대한 결과를 담은 객체
    ){
        if(result.hasErrors()){
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        try {
            TodoListResponseDTO responseDTO = todoService.create(requestDTO, userInfo.getUserId());
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        }


    }

    // 할 일 목록 요청
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
            // JwtAuthFilter에서 시큐리티에게 전역적으로 사용할 수 있는 인증 정보를 등록해 놓았기 때문에
            // @AuthenticationPrincipal을 통해 토큰에 인증된 사용자 정보를 불러올 수 있다.
            @AuthenticationPrincipal TokenUserInfo userInfo
            ){
        log.info("/api/todos GET request");
        TodoListResponseDTO responseDTO = todoService.retrieve(userInfo.getUserId());

        return ResponseEntity.ok().body(responseDTO);
    }// 필터에서 인증정보(userInfo)를 꺼내와서 컨트롤러에서 사용할 수 있다.

    // 할 일 삭제 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("id") String todoId
    ){
        log.info("/api/todos/{} DELETE request!", todoId);

        if(todoId == null || todoId.trim().equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO
                            .builder()
                            .error("ID를 전달해 주세요.")
                            .build());
        }

        try {
            TodoListResponseDTO responseDTO = todoService.delete(todoId, TokenUserInfo.getUserId());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }



    }

    // 할 일 수정하기
    @RequestMapping(method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<?> updateTodo(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody TodoModifyRequestDTO requestDTO,
            BindingResult result,
            HttpServletRequest request
    ){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        log.info("/api/todos {} request!", request.getMethod());
        log.info("modifying dto: {}", requestDTO);

        try {
            TodoListResponseDTO responseDTO = todoService.update(requestDTO, userInfo.getUserId());
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }
    }




}








