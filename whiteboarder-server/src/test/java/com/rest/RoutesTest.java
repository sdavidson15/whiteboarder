package test.java.com.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RoutesTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        Client mockClient = ClientBuilder.newClient();

        // This enables JSON on our mock client
        mockClient.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = mockClient.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testCreateSession() {
        // TODO: Test create session, and confirm status ok and a 9 digit session id.
    }

    @Test
    public void testUploadImage() {
        // TODO: Upload an image binary to server, confirm status ok
    }

    @Test
    public void testGetImage() {
        // Use session id to make sure you're getting the same image back that
        // you posted earlier. You may need to mock some storage stuff
        String resp = target.path("wb/image").request().get(String.class);
        // TODO: Deserialize response
        assertEquals("Ok", resp); // FIXME
    }
}