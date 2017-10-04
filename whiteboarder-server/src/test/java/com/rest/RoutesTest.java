package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class RoutesTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		server = Rest.startServer(true);
		Client mockClient = ClientBuilder.newClient();

		target = mockClient.target(Rest.LOCAL_BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testCreateSession() {
		Response resp = target.path("wb/session").request(APPLICATION_JSON).post(null);
		assertEquals(500, resp.getStatus());
	}

	@Test
	public void testUploadImage() {
		Response resp = target.path("wb/image/sessionID").request(APPLICATION_JSON).post(null);
		assertEquals(200, resp.getStatus());
	}

	@Test
	public void testGetImage() {
		Response resp = target.path("wb/image/sessionID").request().get();
		assertEquals(200, resp.getStatus());
	}
}