package whiteboarder.whiteboarder;

/**
 * SessionInfo only houses the session id of a connected session to make
 * it easily accessible across activities.
 * @author Alexander Campbell
 */
public class SessionInfo {
    private SessionInfo() {
    } // you can't call this

    public static String sessionID;
}
