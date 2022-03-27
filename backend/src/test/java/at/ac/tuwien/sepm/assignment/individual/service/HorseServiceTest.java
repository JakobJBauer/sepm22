package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.AncestorTreeHorse;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;
import at.ac.tuwien.sepm.assignment.individual.entity.Sex;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseServiceTest {

    @Autowired
    HorseService horseService;

    @Test
    public void getAllReturnsAllStoredHorses() {
        List<Horse> horses = horseService.allHorses(new HorseSearchParams());
        assertThat(horses.get(0).getId()).isEqualTo(-15);
        assertThat(horses.get(0).getName()).isEqualTo("Gimme A");
        assertThat(horses.get(6).getId()).isEqualTo(-9);
        assertThat(horses.get(6).getName()).isEqualTo("Ash9");
        assertThat(horses.get(10).getId()).isEqualTo(-5);
        assertThat(horses.get(10).getName()).isEqualTo("Ash5");
    }

    @Test
    public void getAncestorTree() {
        assertThat(
                horseService.getAncestorTree(1)
                        .get(5)
                        .getParents()[0]
                        .getId()
        ).isEqualTo(-12);
    }

    @Test
    public void invalidMaxGenerationThrowsValidationException() {
        assertThatThrownBy(
                () -> horseService.getAncestorTree(-4)
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    public void invalidIdThrowsNoResultException() {
        assertThatThrownBy(
                () -> horseService.updateHorse(
                        new Horse(
                                (long) -99,
                                "Invalid",
                                null,
                                LocalDate.now(),
                                Sex.MALE,
                                null,
                                new Long[0]
                        )
                )
        ).isInstanceOf(NoResultException.class);
    }
}
