package org.java.lessons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
		static final String url = "jdbc:mysql://localhost:3306/db-nation";
		static final String user = "root";
		static final String pwd = "";
		
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Cerca una nazione");
		String value = sc.nextLine();
		
		final String param = "%" + value +  "%";
		
		final String sql = " SELECT c.name, c.country_id , r.name , c2.name  \r\n"
				+ " FROM countries c \r\n"
				+ "	JOIN regions r \r\n"
				+ "	ON c.region_id = r.region_id \r\n"
				+ "	JOIN continents c2 \r\n"
				+ "	ON r.continent_id = c2.continent_id \r\n"
				+ " WHERE c.name LIKE ?";
		
		try (Connection conn = DriverManager.getConnection(url, user, pwd)) {
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, param);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next())
				{
					String countryName = rs.getString(1);
					int countryId = rs.getInt(2);
					String regionName = rs.getString(3);
					String continentName = rs.getString(4);
					
					System.out.println("Nazione: " + countryName + " ID: " + countryId);
					System.out.println("Regione: " + regionName);
					System.out.println("Continente: " + continentName);
					
					System.out.println("\n----------------\n");
				}
				
			} catch (Exception e)
			{
				System.err.println(e.getMessage());
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
