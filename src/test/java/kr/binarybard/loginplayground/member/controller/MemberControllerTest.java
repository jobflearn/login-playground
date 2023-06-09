package kr.binarybard.loginplayground.member.controller;

import kr.binarybard.loginplayground.config.exception.DuplicateMemberException;
import kr.binarybard.loginplayground.member.domain.Member;
import kr.binarybard.loginplayground.member.dto.SignUpRequest;
import kr.binarybard.loginplayground.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/members/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    public void testSignUp_Get() throws Exception {
        mockMvc.perform(get("/members/signup"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("member"))
            .andExpect(view().name("sign-up"));
    }

    @Test
    public void testSignUp_Post_Success() throws Exception {
        when(memberService.save(any(SignUpRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@test.com")
                .param("password", "test1234"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/members/login"));
    }

    @Test
    public void testSignUp_Post_Fail_DuplicateMemberException() throws Exception {
        doThrow(DuplicateMemberException.class).when(memberService).save(any());

        mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@test.com")
                .param("password", "test1234"))
            .andExpect(status().isOk())
            .andExpect(view().name("sign-up"));
    }
}
