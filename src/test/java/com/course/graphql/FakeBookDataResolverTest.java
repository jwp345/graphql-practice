package com.course.graphql;

import com.course.graphql.generated.client.BooksByReleasedGraphQLQuery;
import com.course.graphql.generated.client.BooksGraphQLQuery;
import com.course.graphql.generated.client.BooksProjectionRoot;
import com.course.graphql.generated.types.Author;
import com.course.graphql.generated.types.Book;
import com.course.graphql.generated.types.ReleaseHistoryInput;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FakeBookDataResolverTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    Faker faker;

    @Test
    void testAllBooks() {
        var grapqlQuery = new BooksGraphQLQuery.Builder().build();
        var projectionRoot = new BooksProjectionRoot().title()
                .author().name()
                .originCountry()
                .getRoot().released().year();

        var graphqlQueryRequest = new GraphQLQueryRequest(grapqlQuery, projectionRoot).serialize();

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQueryRequest, "data.books[*].title");

            assertNotNull(titles);
            assertFalse(titles.isEmpty());

            List<Author> authors = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                    graphqlQueryRequest, "data.books[*].author",
                    new TypeRef<List<Author>>() {}
            );

            assertNotNull(authors);
            assertEquals(titles.size(), authors.size());

            List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                    graphqlQueryRequest, "data.books[*].released.year",
                    new TypeRef<List<Integer>>() {
                    }
            );

            assertNotNull(releaseYears);
            assertEquals(titles.size(), releaseYears.size());
    }

    @Test
    void testBooksWithInput() {
        int expectedYear = faker.number().numberBetween(2019, 2021);
        boolean expectedPrintedEdition = faker.bool().bool();

        var releaseHistoryInput = ReleaseHistoryInput.newBuilder()
                .year(expectedYear)
                .printedEdition(expectedPrintedEdition)
                .build();

        var graphqlQuery = BooksByReleasedGraphQLQuery.newRequest()
                .releasedInput(releaseHistoryInput)
                .build();

        var projectionRoot = new BooksProjectionRoot()
                .released().year().printedEdition();

        var graphqlQueryRequest = new GraphQLQueryRequest(
                graphqlQuery, projectionRoot
        ).serialize();

        List<Integer> releaseYear = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQueryRequest, "data.booksByReleased[*].released.year"
        );

        Set<Integer> uniqueReleaseyears = new HashSet<>(releaseYear);

        assertNotNull(uniqueReleaseyears);
        assertTrue(uniqueReleaseyears.size() <= 1);

        if(!uniqueReleaseyears.isEmpty()) {
            assertTrue(uniqueReleaseyears.contains(expectedYear));
        }

        List<Boolean> releasedPrintedEditions = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQueryRequest, "data.booksByReleased[*].released.printedEdition"
        );
        Set<Boolean> uniqueReleasePrintedEditions = new HashSet<>(releasedPrintedEditions);

        assertNotNull(uniqueReleasePrintedEditions);
        assertTrue(uniqueReleasePrintedEditions.size() <= 1);

        if(!uniqueReleasePrintedEditions.isEmpty()) {
            assertTrue(uniqueReleasePrintedEditions.contains(expectedPrintedEdition));
        }
    }
}
