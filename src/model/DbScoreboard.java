package model;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;

// TODO: Run in seperate thread
public class DbScoreboard {
	
	public DbScoreboard() {
		try {
			Class.forName("org.sqlite.JDBC");
			ensureStructure();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Makes sure that the necessary table is there
	private void ensureStructure() {
		try (Statement statement = openStatement()) {
			ResultSet rs = statement.executeQuery("SELECT time FROM scores");
		} catch (SQLException e) {
			try {
                Statement s = openStatement();
				s.executeUpdate("CREATE TABLE scores(" +
						"time INT)");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

    private @NotNull Statement openStatement() throws SQLException {
		Connection c = DriverManager.getConnection("jdbc:sqlite:Scoreboard.db");
		c.setAutoCommit(false);
		return c.createStatement();
	}

	private void closeStatement(Statement statement) {
		try (Connection connection = statement.getConnection();) {
			connection.commit();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<Long> getTop(int n) {
		try (Statement statement = openStatement();) {
			LinkedList<Long> result = new LinkedList<>();
			ResultSet rs = statement.executeQuery(	"SELECT * " +
					"FROM scores " +
					"ORDER BY time " +
					"LIMIT " + n);

			while (rs.next()) {
				result.add(rs.getLong("time"));
			}
			return result;
		} catch (SQLException e) {
            // FIXME
			e.printStackTrace();
			return new LinkedList<>();
		}
	}
	
	public LinkedList<Long> loadAllScores() {

		ResultSet rs;
		LinkedList<Long> result = new LinkedList<>();
		try (Statement s = openStatement();) {
		    rs = s.executeQuery("SELECT time FROM scores");
			result = new LinkedList<>();
		    while (rs.next()) {
		    	result.add(rs.getLong("time"));
		    }
		} catch (SQLException e) {
            // FIXME
			e.printStackTrace();
		}
		return result;
	}
	
	private void saveScore(long score){
		try (Statement s = openStatement()) {
			s.executeUpdate("INSERT INTO scores VALUES (" + score + ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(long score) {
		saveScore(score);
	}
}
