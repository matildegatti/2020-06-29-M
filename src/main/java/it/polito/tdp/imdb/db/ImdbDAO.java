package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenze;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer,Director> idMap){
		String sql = "SELECT * FROM directors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				idMap.put(director.getId(), director);
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Director> getVertici(Map<Integer,Director> idMap, int anno){
		String sql="SELECT md.director_id AS id FROM movies m, movies_directors md "
				+ "WHERE m.id=md.movie_id && m.year=? GROUP BY md.director_id";
		List<Director> result=new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("id"))) 
					result.add(idMap.get(res.getInt("id")));
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenze> getAdiacenze(Map<Integer,Director> idMap, int anno){
		String sql="SELECT md1.director_id AS id1, md2.director_id AS id2, COUNT(DISTINCT r1.actor_id) AS peso "
				+ "FROM roles r1, roles r2, movies_directors md1, movies_directors md2, movies m1, movies m2 "
				+ "WHERE md1.movie_id = r1.movie_id && m1.id=md1.movie_id && md2.movie_id=r2.movie_id && m2.id=md2.movie_id "
				+ "&& md1.director_id>md2.director_id && r1.actor_id=r2.actor_id && m1.year=? && m1.year=m2.year "
				+ "GROUP BY md1.director_id, md2.director_id";
		List<Adiacenze> result=new ArrayList<Adiacenze>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int id1=res.getInt("id1");
				int id2=res.getInt("id2");
				if(idMap.containsKey(id1) && idMap.containsKey(id2)) { 
					result.add(new Adiacenze(idMap.get(id1), idMap.get(id2), res.getDouble("peso")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
