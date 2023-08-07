package com.team.heyyo.todolist.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.todolist.domain.TodoList;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoListRepositoryTest {

    @Autowired
    TodoListRepository todoListRepository;

    @Autowired
    EntityManager entityManager;

    private final static long TODOLIST_ID = 30L;

    @Test
    @DisplayName("todoList 생성")
    public void saveTodoList() {
        // given
        TodoList todoList = createTodoList("data");

        // when
        TodoList result = todoListRepository.save(todoList);

        // then
        assertThat(result.getUserId()).isEqualTo(TODOLIST_ID);
    }

    @Test
    @DisplayName("사용자 정보로 todoList 탐색")
    public void searchTodoListByUserEmail() {
        // given
        TodoList todoList = createTodoList("data");
         todoListRepository.save(todoList);

        // when
        TodoList result = todoListRepository.findTodoListByUserId(TODOLIST_ID).get();

        // then
        assertThat(result.getUserId()).isEqualTo(TODOLIST_ID);
        assertThat(result.getData()).isEqualTo("data");
    }

    @Test
    @DisplayName("todoList 제거")
    public void deleteTodoList() {
        // given
        long todoListId = 1L;
        todoListRepository.save(createTodoList(todoListId));

        // when
        todoListRepository.deleteTodoListByTodoListId(todoListId);
        entityManager.flush();
        entityManager.clear();

        Optional<TodoList> result = todoListRepository.findById(todoListId);

        // then
        assertThat(result.isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("진행중인 todoList 가져오기")
    public void getProcessTodoList() {
        // given
        todoListRepository.save(createTodoList("data" , false));
        todoListRepository.save(createTodoList("data" , true));
        todoListRepository.save(createTodoList("data" , true));

        // when
        List<TodoList> result = todoListRepository.findTodoListInProgressByUserId(TODOLIST_ID);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("달성한 todoList 가져오기")
    public void getAchievedTodoList() {
        // given
        todoListRepository.save(createTodoList("data" , false));
        todoListRepository.save(createTodoList("data" , true));
        todoListRepository.save(createTodoList("data" , true));

        // when
        List<TodoList> result = todoListRepository.findAchievedTodoListByUserId(TODOLIST_ID);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("달성한 todoList 날짜 별로 가져오기")
    public void getTodoListByDateAchieved() {
        // given
        todoListRepository.save(createTodoList("data" , false));
        todoListRepository.save(createTodoList("data" , true));
        todoListRepository.save(createTodoList("data" , true));

        // when
        List<TodoList> result = todoListRepository.getTodoListByDateAchieved(TODOLIST_ID , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    public TodoList createTodoList(long todoListId) {
        return TodoList.builder()
                .todoListId(todoListId)
                .data("data")
                .userId(TODOLIST_ID)
                .isComplete(false)
                .build();
    }

    public TodoList createTodoList(String data) {
        return TodoList.builder()
                .data(data)
                .userId(TODOLIST_ID)
                .isComplete(false)
                .build();
    }

    public TodoList createTodoList(String data , boolean isComplete) {
        return TodoList.builder()
                .data(data)
                .userId(30L)
                .isComplete(isComplete)
                .completedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

}
