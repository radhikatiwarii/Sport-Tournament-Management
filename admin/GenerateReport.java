package admin;
class GenerateReport{
public static void generateReport() {
    try {
        Connection con = Databaseconnection.getConnection();
        
        // SQL Query to fetch assigned players and coaches
        String query = "SELECT p.Player_Id, u.user_name AS player_name, p.team_id, p.assigned_coach_id, " +
                       "cu.user_name AS coach_name, c.specialization, c.years_of_experience " +
                       "FROM players p " +
                       "LEFT JOIN users u ON p.user_id = u.user_id " +
                       "LEFT JOIN coaches c ON p.assigned_coach_id = c.coach_id " +
                       "LEFT JOIN users cu ON c.user_id = cu.user_id";
        
        PreparedStatement pre = con.prepareStatement(query);
        ResultSet re = pre.executeQuery();
        
        System.out.println("+------------+----------------+------------+------------+----------------+------------------+-------------------+");
        System.out.printf("| %-10s | %-14s | %-10s | %-10s | %-14s | %-16s | %-17s |\n",
                          "Player ID", "Player Name", "Team ID", "Coach ID", "Coach Name", "Specialization", "Experience");
        System.out.println("+------------+----------------+------------+------------+----------------+------------------+-------------------+");
        
        while (re.next()) {
            System.out.printf("| %-10s | %-14s | %-10s | %-10s | %-14s | %-16s | %-17d |\n",
                              re.getString("Player_Id"),
                              re.getString("player_name"),
                              re.getString("team_id"),
                              re.getString("assigned_coach_id"),
                              re.getString("coach_name"),
                              re.getString("specialization"),
                              re.getInt("years_of_experience"));
        }
        
        System.out.println("+------------+----------------+------------+------------+----------------+------------------+-------------------+");

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}