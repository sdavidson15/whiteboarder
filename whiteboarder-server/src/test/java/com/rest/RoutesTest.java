package com.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RoutesTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		server = Rest.startServer(null, true);
		Client mockClient = ClientBuilder.newClient();

		target = mockClient.target(Rest.BASE_URI);
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
		// TODO: Upload an image to the server, confirm status ok.
	}

	@Test
	public void testGetImage() {
		String resp = target.path("wb/image/sessionID").request().get(String.class);
		// TODO: Deserialize response
		assertEquals("This should be JSON", resp);
	}

}