
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class dataClean {
	
	public static void main(String[] args) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/movie_recommend", "root", "");
			Statement st = con.createStatement();
			
//			cleanMovieData(st);
//			cleanRatingsData(st);
			/*ArrayList<String> topMoviesUser = topRated(100);
			for(String temp: topMoviesUser)
			{
				System.out.println(temp);
			//	len--;
			}
			*/
			addMovie("Forrest", 4, 100);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	static ArrayList<String> topMoviesUser;
	static ArrayList<Integer> topMovieID;

	
	public static ArrayList<String> topRated (int user){
		topMoviesUser = new ArrayList<>();
		topMovieID = new ArrayList<>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_recommend", "root", "");
			Statement movies = connect.createStatement();
			
			ResultSet top = movies.executeQuery("select movie_id from rating where user_id = "+user+
					" order by ratings desc limit 7");
			while(top.next()){
				topMovieID.add(top.getInt("movie_id"));
			}
			
			if(top != null){
				top.close();
			}
			
			int thisID = 0;
			for(int i = 0; i < topMovieID.size(); i++){
				thisID = topMovieID.get(i);
				ResultSet mName = movies.executeQuery("select movie_name from movie where movie_id = "+thisID);
				if(mName.next()){
					topMoviesUser.add(mName.getString("movie_name").trim());
				}
				if(mName != null){
					mName.close();
				}
			}
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return topMoviesUser;
	}
	
	
	public static void addMovie(String name, int rating, int user){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection c = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/movie_recommend", "root", "");
			Statement newMovie = c.createStatement();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection mIDConnect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/movie_recommend", "root", "");
			Statement mIDStat = mIDConnect.createStatement();
			
			ResultSet mCount = newMovie.executeQuery("select count(*) from movie");
			int movies = 0;
			if(mCount.next()){
				movies = mCount.getInt(1);
			}
			if(mCount != null){
				mCount.close();
			}
			
			ResultSet ifExists = newMovie.executeQuery("select count(*) from movie where movie_name = '"+name.trim()+"'");
			int getCount = 0, getMID = 0;
			if(ifExists .next()){
				getCount = ifExists.getInt(1);
				if(getCount == 0){
					newMovie.executeUpdate("insert into movie (movie_id, movie_name, url) values ("+(movies+1)+", '"+name+"', 'NULL')");
					newMovie.executeUpdate("insert into rating (user_id, movie_id, ratings) values ("+user+","+(movies+1)+","+rating+")");
				}
				else{
					ResultSet mIdRes = mIDStat.executeQuery("select movie_id from movie where movie_name = '"+name+"'");
					if(mIdRes.next()){
						getMID = mIdRes.getInt("movie_id");
					}
					if(mIdRes != null){
						mIdRes.close();
					}
					mIDStat.executeUpdate("insert into rating (user_id, movie_id, ratings) values ("+user+","+getMID+","+rating+")");
				}
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void cleanMovieData(Statement st) {
		try {
			st.executeUpdate("alter table movie drop column release_date");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void cleanRatingsData(Statement st) {
		try {
			st.executeUpdate("alter table rating drop column timestamp");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
