package com.mjc.school.repository;
import com.mjc.school.repository.impl.AuthorRepositoryImpl;
import com.mjc.school.repository.impl.NewsRepositoryImpl;
import com.mjc.school.repository.impl.TagRepositoryImpl;
import com.mjc.school.repository.model.Author;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(classes = ConfigureTestDatabase.class)
public class BaseRepositoryTest {

    @Autowired ApplicationContext applicationContext;

    @Autowired
    private List<BaseRepository<?, Long>> repositories;


    private AuthorRepositoryImpl authorRepositoryImpl;


    private NewsRepositoryImpl newsRepositoryImpl;


    private TagRepositoryImpl tagRepositoryImpl;

    @Test
    @Order(1)
    void testRepositoriesExist() {
//        assertThat(repositories).isNotNull().isNotEmpty();
//        assertThat(repositories.size()).isEqualTo(3);
//        assertThat(authorRepository).isNotNull();
//        assertThat(newsRepository).isNotNull();
//        assertThat(tagRepository).isNotNull();
        String[] repositoryBeans = applicationContext.getBeanNamesForType(org.springframework.data.repository.Repository.class);
        assertNotNull(repositoryBeans);
        assertTrue(repositoryBeans.length > 0, "At least one repository bean should be found");
    }

    @TestFactory
    @Order(2)
    Stream<DynamicNode> testAllRepositories() {
        return repositories.stream()
                .map(repo -> dynamicContainer(
                        repo.getClass().getSimpleName(),
                        getTests(repo)
                ));
    }

    private Stream<DynamicTest> getTests(BaseRepository<?, Long> repository) {
        return Stream.of(
                dynamicTest("readAll() should return a list", () -> {
                    List<?> result = repository.readAll();
                    assertThat(result).isNotNull().isInstanceOf(List.class);
                }),
                dynamicTest("readById(0L) should return empty Optional", () -> {
                    assertThat(repository.readById(0L)).isEmpty();
                })
        );
    }

    @Test
    @Order(3)
    void testAuthorRepositoryCreateAndRead() {
        Author author = new Author();
        author.setName("Test Author");
        Author saved = authorRepositoryImpl.create(author);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(authorRepositoryImpl.readById(saved.getId())).isPresent();
    }
}
