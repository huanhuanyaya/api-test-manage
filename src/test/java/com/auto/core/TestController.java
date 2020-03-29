package com.auto.core;

import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//支持junit + mockmvc

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestController {

    @Autowired
    private MockMvc mockMvc;
    String sessionId;

    @Before
    @Test
    public void testLogin() throws Exception {
        String resultJson = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
        .param("username","t01@123.com")
        .param("password","96e79218965eb72c92a549dd5a330112")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        //将明文转成密文  md5工具类
        System.out.println(resultJson);
        sessionId = JsonPath.read(resultJson,"$.message");
    }

    @Ignore //忽略
    @Test
    public void testFind() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/find")
                .header("Authorization",sessionId)
                .param("username","t01@123.com")
        ).andDo(print()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.startsWith("账号已存在!"))
        ).andReturn();

    }

    //参数为json的
    @Test
    public void testAddProject2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/project/addProject2")
                .header("Authorization",sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ck\",\"host\":\"http://admin.huanhuan.org\"}")
        ).andDo(print()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.startsWith("新增项目成功"))
        ).andReturn();

    }


    /**
     * junit注解:
     * @Before 用于初始化测试对象,测试对象以实例变量存放
     * @After 用于清理@before创建的对象
     * @BeforeClass 用于初始化耗时资源,以静态变量存放
     * @AfterClass 用于清理@BeforeClass创建的资源
     */



}
