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
		
		System.out.print("Cerca una nazione ");
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
		
		System.out.print("Inserisci un id per ottenere i dettagli della nazione ");
		int idNation = Integer.valueOf(sc.nextLine());
		
		final String sql2 = "SELECT c.name, cs.`year` , cs.population , cs.gdp  \r\n"
							+ " FROM countries c \r\n"
							+ "	JOIN  country_stats cs \r\n"
							+ "	ON c.country_id = cs.country_id \r\n"
							+ "	JOIN country_languages cl \r\n"
							+ "	ON c.country_id = cl.country_id \r\n"
							+ "	JOIN languages l \r\n"
							+ "	ON cl.language_id = l.language_id \r\n"
							+ " WHERE c.country_id = ? \r\n"
							+ " ORDER BY cs.`year` DESC \r\n"
							+ " LIMIT 1";
		
		final String sqlLang = "SELECT l.`language`  \r\n"
							+ " FROM countries c \r\n"
							+ "	JOIN  country_stats cs \r\n"
							+ "	ON c.country_id = cs.country_id \r\n"
							+ "	JOIN country_languages cl \r\n"
							+ "	ON c.country_id = cl.country_id \r\n"
							+ "	JOIN languages l \r\n"
							+ "	ON cl.language_id = l.language_id \r\n"
							+ " WHERE c.country_id = ? \r\n"
							+ " GROUP BY l.`language` \r\n"
							+ " ORDER BY cs.`year` DESC \r\n";

		try (Connection conn = DriverManager.getConnection(url, user, pwd)) {
			try {
				conn.setAutoCommit(false);
				
				PreparedStatement ps = conn.prepareStatement(sql2);
				ps.setInt(1, idNation);;
				ResultSet rs = ps.executeQuery();
				
				PreparedStatement psLang = conn.prepareStatement(sqlLang);
				psLang.setInt(1, idNation);
				ResultSet rsLang = psLang.executeQuery();
				
				String countryName = "";
				int year = 0;
				long population = 0;
				long gdp =0;
				String languages = "";
				
				while(rs.next())
				{
					countryName = rs.getString(1);
					
					year = rs.getInt(2);
					population = rs.getLong(3);
					gdp = rs.getLong(4);				
					
				}
				
				
				while(rsLang.next())
				{
					languages += rsLang.getString(1) + " ";
				}
				
					System.out.println("Dettagli nazione: " + countryName);
					System.out.println("Lingue: " + languages );
					System.out.println("Dati piu' recenti: " + year);
					System.out.println("Popolazione: " + population);
					System.out.println("GDP: " + gdp);
					
					System.out.println("\n----------------\n");
				
			} catch (Exception e)
			{
				System.err.println(e.getMessage());
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		sc.close();
	}
}
