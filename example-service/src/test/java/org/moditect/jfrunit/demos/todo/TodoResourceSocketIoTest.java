package org.moditect.jfrunit.demos.todo;

import static org.moditect.jfrunit.EnableEvent.StacktracePolicy.INCLUDED;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.moditect.jfrunit.demos.todo.testutil.PostgresResource;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.moditect.jfrunit.EnableEvent;
import org.moditect.jfrunit.JfrEvents;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jdk.jfr.consumer.RecordedEvent;

@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
@TestMethodOrder(value = OrderAnnotation.class)
public class TodoResourceSocketIoTest {

    private static final int ITERATIONS = 10;

    public JfrEvents jfrEvents = new JfrEvents();

    @ConfigProperty(name="jfrunit.database.port")
    public int databasePort;

    @Test
    @Order(1)
    public void setupTodos() {
        Random r = new Random();

        for (int i = 1; i<= 20; i++) {
            given()
                .when()
                    .body(String.format("""
                          {
                            "title" : "Learn Quarkus",
                            "priority" : 1,
                            "userId" : %s
                          }
                          """,
                          r.nextInt(5) + 1)
                    )
                    .contentType(ContentType.JSON)
                    .post("/todo")
                .then()
                    .statusCode(201);
            given()
                .when()
                    .body(String.format("""
                          {
                            "title" : "Learn Quarkus",
                            "priority" : 1,
                            "userId" : %s,
                            "avatar" : "%s"
                          }
                          """,
                          r.nextInt(5) + 1,
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISBIZWxsbyBXb3JsZCwgaGVsbG8gSmZyVW5pdCEg" +
                          "SGVsbG8gV29ybGQsIGhlbGxvIEpmclVuaXQhIEhlbGxvIFdvcmxkLCBoZWxsbyBKZnJVbml0ISA=")
                    )
                    .contentType(ContentType.JSON)
                    .post("/todo/todo-with-avatar")
                .then()
                    .statusCode(201);
        }
    }

//    @Test
    @Order(2)
    @EnableEvent(value="jdk.SocketRead", stackTrace=INCLUDED)
    @EnableEvent(value="jdk.SocketWrite", stackTrace=INCLUDED)
    public void retrieveTodoBaseline() throws Exception {
        Random r = new Random();

        for (int i = 1; i<= ITERATIONS; i++) {
            int id = r.nextInt(20) + 1;

            given()
                .when()
                    .contentType(ContentType.JSON)
                    .get("/todo/" + id)
                .then()
                    .statusCode(200);
        }

        jfrEvents.awaitEvents();

        jfrEvents.filter(this::isDatabaseIoEvent)
            .forEach(System.out::println);

        long sum = jfrEvents.filter(this::isDatabaseIoEvent)
            .mapToLong(this::getBytesReadOrWritten)
            .sum();

        System.out.println("### Event count: " + jfrEvents.filter(this::isDatabaseIoEvent).count());
        System.out.println("### I/O bytes sum: " + sum);
        System.out.println("### I/O bytes per request: " + sum / ITERATIONS);
    }

    @Test
    @Order(3)
    @EnableEvent(value="jdk.SocketRead", threshold = 0, stackTrace=INCLUDED)
    @EnableEvent(value="jdk.SocketWrite", threshold = 0, stackTrace=INCLUDED)
    public void retrieveTodoShouldYieldExpectedIo() throws Exception {
        Random r = new Random();
        HttpClient client = HttpClient.newBuilder()
                .build();

        for (int i = 1; i<= ITERATIONS; i++) {
            executeRequest(r.nextInt(20) + 1, client);
        }

        jfrEvents.awaitEvents();

        long count = jfrEvents.filter(this::isDatabaseIoEvent).count();
        assertThat(count / ITERATIONS).isEqualTo(4).describedAs("write + read per statement, write + read per commit");

        long bytesReadOrWritten = jfrEvents.filter(this::isDatabaseIoEvent)
            .mapToLong(this::getBytesReadOrWritten)
            .sum();

        assertThat(bytesReadOrWritten / ITERATIONS).isLessThan(480);
    }

    @Test
    @Order(4)
    @EnableEvent(value="jdk.SocketRead", threshold = 0, stackTrace=INCLUDED)
    @EnableEvent(value="jdk.SocketWrite", threshold = 0, stackTrace=INCLUDED)
    public void retrieveTodoIoRegression() throws Exception {
        Random r = new Random();
        HttpClient client = HttpClient.newBuilder()
                .build();

        for (int i = 1; i<= ITERATIONS; i++) {
            executeRequest("with-io-regression/", r.nextInt(20) + 1, client);
        }

        jfrEvents.awaitEvents();

        long count = jfrEvents.filter(this::isDatabaseIoEvent).count();

        // expected to fail
//        assertThat(count / ITERATIONS).isEqualTo(4).describedAs("write + read per statement, write + read per commit");

        long bytesReadOrWritten = jfrEvents.filter(this::isDatabaseIoEvent)
            .mapToLong(this::getBytesReadOrWritten)
            .sum();

        // expected to fail
//        assertThat(bytesReadOrWritten / ITERATIONS).isLessThan(500);
    }

    private long getBytesReadOrWritten(RecordedEvent re) {
        return re.getEventType().getName().equals("jdk.SocketRead") ? re.getLong("bytesRead") : re.getLong("bytesWritten");
    }

    private boolean isDatabaseIoEvent(RecordedEvent re) {
        return ((re.getEventType().getName().equals("jdk.SocketRead") ||
                re.getEventType().getName().equals("jdk.SocketWrite")) &&
                re.getInt("port") == databasePort);
    }

    private void executeRequest(long id, HttpClient client) throws URISyntaxException, IOException, InterruptedException {
        executeRequest("", id, client);
    }

    private void executeRequest(String pathPrefix, long id, HttpClient client) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/todo/" + pathPrefix + id))
                .headers("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
    }
}
