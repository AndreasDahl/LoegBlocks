package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		Statement s = openStatement();
		try {
			ResultSet rs = s.executeQuery("SELECT time FROM scores");
		} catch (SQLException e) {
			try {
				s.executeUpdate("CREATE TABLE scores(" +
						"time INT)");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		closeStatement(s);
	}
	
	private Statement openStatement() {
		Connection c;
		try {
			c = DriverManager.getConnection("jdbc:sqlite:Scoreboard.db");
			c.setAutoCommit(false);
			return c.createStatement();
		} catch (SQLException e) {
			//TODO: Something Smart
			e.printStackTrace();
			return null;
		}
	}
	
	private void closeStatement(Statement s) {
		try {
			Connection c = s.getConnection();
			c.commit();
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<Long> getTop(int n) {
		Statement s = openStatement();
		LinkedList<Long> result = new LinkedList<>();
		try {
			ResultSet rs = s.executeQuery(	"SELECT * " + 
					"FROM scores " +
					"ORDER BY time " +
					"LIMIT " + n);

			while (rs.next()) {
				result.add(rs.getLong("time"));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return new LinkedList<>();
		}
		finally {
			closeStatement(s);
		}
	}
	
	public LinkedList<Long> loadAllScores() {
		Statement s = openStatement();
		ResultSet rs;
		LinkedList<Long> result = new LinkedList<>();
		try {
		    rs = s.executeQuery("SELECT time FROM scores");
			result = new LinkedList<>();
		    while (rs.next()) {
		    	result.add(rs.getLong("time"));
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeStatement(s);
		return result;
	}
	
	private void saveScore(long score) {
		Statement s = openStatement();
		try {
			s.executeUpdate("INSERT INTO scores VALUES (" + score + ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeStatement(s);
	}
	
	public void add(long score) {
		saveScore(score);
	}
}
