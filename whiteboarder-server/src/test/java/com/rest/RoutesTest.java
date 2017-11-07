package com.rest;

import static org.junit.Assert.assertEquals;

import com.Main;
import com.core.Context;

import java.util.Map;
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
		Map<String, String> cfg = Main.getConfig("local-config.txt");
		Context ctx = new Context(null, null, true);
		server = Rest.startServer(ctx, cfg.get("rest-uri"), Integer.parseInt(cfg.get("port")));
		Client mockClient = ClientBuilder.newClient();

		target = mockClient.target(cfg.get("rest-uri"));
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
		String resp = target.path("whiteboarder/image/sessionID").request().get(String.class);
		// TODO: Deserialize response
		assertEquals("This should be JSON", resp);
	}

}