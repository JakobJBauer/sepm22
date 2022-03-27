package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.beans.FeatureDescriptor;
import java.time.LocalDate;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;
import at.ac.tuwien.sepm.assignment.individual.entity.Sex;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.number.PercentStyleFormatter;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseDaoTest {

    @Autowired
    HorseDao horseDao;

    @Test
    public void getAllReturnsAllStoredHorses() {
        List<Horse> horses = horseDao.getAll(new HorseSearchParams());
        assertThat(horses.size()).isEqualTo(15);
        assertThat(horses.get(0).getId()).isEqualTo(-15);
        assertThat(horses.get(0).getName()).isEqualTo("Gimme A");
        assertThat(horses.get(6).getId()).isEqualTo(-9);
        assertThat(horses.get(6).getName()).isEqualTo("Ash9");
        assertThat(horses.get(10).getId()).isEqualTo(-5);
        assertThat(horses.get(10).getName()).isEqualTo("Ash5");
    }

    @Test
    public void createAndDeleteAll() {
        var result = horseDao.createHorse(
                new Horse(
                        "name",
                        "description",
                        LocalDate.now(),
                        Sex.FEMALE,
                        null,
                        new Long[]{(long) -1}
                )
        );
        assertThat(result.getId()).isEqualTo(1);
        horseDao.deleteHorseById(result.getId());
    }

    @Test
    public void getByIdThrowsNoResult() {
        assertThatThrownBy(
                () -> horseDao.getHorseById((long) -99)
        ).isInstanceOf(NoResultException.class);
    }

    @Test
    public void updateHorseInvalidName() {
        assertThatThrownBy(
                () -> horseDao.updateHorse(
                        new Horse((long) -15,
                                null,
                                null,
                                LocalDate.now(),
                                Sex.FEMALE,
                                null,
                                new Long[]{(long) -13}
                        )
                )
        ).isInstanceOf(PersistenceException.class);
    }
}
