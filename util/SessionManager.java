package util;

public class SessionManager {
    private static int userId;

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    private static int Player_Id;

    public static void setPlayer_Id(int id) {
        Player_Id = id;
    }

    public static int getPlayer_Id() {
        return Player_Id;
    }
    
    private static int organizerId;

    public static void setOrganizerId(int id) {
        organizerId = id;
    }

    public static int getOrganizerId() {
        return organizerId;
    }
}