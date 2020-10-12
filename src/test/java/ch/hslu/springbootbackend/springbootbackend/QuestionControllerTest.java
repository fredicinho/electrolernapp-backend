package ch.hslu.springbootbackend.springbootbackend;

import ch.hslu.springbootbackend.springbootbackend.Controller.QuestionController;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.SpringVersion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    public final String path = "/api/v1/questions/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QuestionController questionController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(questionController).isNotNull();
    }
    @Test
    public void getAllQuestion() throws Exception {

        Answer answer = new Answer("correct");
        Answer answer2 = new Answer("false");
        List<Answer> answers = new LinkedList<>();
        answers.add(answer);
        answers.add(answer2);
        Question question = new Question("A testcase", answers, answers.get(1));
        question.setCorrectAnswer(answer);

        List<Question> allQuestions = singletonList(question);
        given(questionController.getAllQuestions()).willReturn(allQuestions);

        mvc.perform(get(path)
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(question.getId()))
                .andExpect(jsonPath("$[0].questionphrase").value(question.getQuestionphrase()))
                .andExpect(jsonPath("$[0].possibleAnswers").isArray())
                .andExpect(jsonPath("$[0].correctAnswer.id").value(question.getCorrectAnswer().getId()))
    .andReturn();

        //wasnt able to check if both contains same possible answers, but correct answer is right...
    }
    @Test
    public void getQuestionById() throws Exception {
        Answer answer = new Answer("correct");
        Answer answer2 = new Answer("false");
        List<Answer> answers = new LinkedList<>();
        answers.add(answer);
        answers.add(answer2);
        Question question = new Question("A testcase", answers, answers.get(1), 1);
        question.setCorrectAnswer(answer);
        when(questionController.getQuestionById(question.getId())).thenReturn(question);

        mvc.perform(get(path + question.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(question.getId()));

        System.out.println(question.toString());
    }

    @Test
    public void insertNewQuestion() throws Exception {
        Answer answer = new Answer("correct");
        Answer answer2 = new Answer("false");
        List<Answer> answers = new LinkedList<>();
        answers.add(answer);
        answers.add(answer2);
        Question question = new Question("A testcase", answers, answers.get(1), 1);
        when(questionController.newQuestion(question)).thenReturn(question);
        String inputJson = asJsonString(question);
        System.out.println(inputJson);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        //no checking if return is correct when status is ok

    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
