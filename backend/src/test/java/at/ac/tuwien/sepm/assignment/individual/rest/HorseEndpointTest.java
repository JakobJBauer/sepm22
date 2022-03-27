package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.BasicHorseInputDto;
import at.ac.tuwien.sepm.assignment.individual.dto.BasicHorseOutputDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Sex;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
public class HorseEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void gettingAllHorses() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<BasicHorseOutputDto> horseResult = objectMapper.readerFor(BasicHorseOutputDto.class).<BasicHorseOutputDto>readValues(body).readAll();

        assertThat(horseResult).isNotNull();
        assertThat(horseResult.size()).isEqualTo(1);
        assertThat(horseResult.get(0).id()).isEqualTo(-1);
        assertThat(horseResult.get(0).name()).isEqualTo("Wendy");
    }

    @Test
    public void gettingNonexistentUrlReturns404() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/asdf123")
                ).andExpect(status().isNotFound());
    }

    @Test
    public void gettingParentSearch() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses/parentsearch")
                        .param("resultSize", "5")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<BasicHorseOutputDto> horseResult = objectMapper.readerFor(BasicHorseOutputDto.class).<BasicHorseOutputDto>readValues(body).readAll();

        assertThat(horseResult).isNotNull();
        assertThat(horseResult.size()).isEqualTo(5);
        assertThat(horseResult.get(0).id()).isEqualTo(-15);
        assertThat(horseResult.get(0).name()).isEqualTo("Gimme A");
    }

    @Test
    public void postingBodyReturns201() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("name","george");
        body.put("birthdate","2020-09-09");
        body.put("sex","MALE");

         mockMvc.
                perform(
                        MockMvcRequestBuilders
                                .post("/horses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                ).andExpect(status().isCreated());
    }
}
