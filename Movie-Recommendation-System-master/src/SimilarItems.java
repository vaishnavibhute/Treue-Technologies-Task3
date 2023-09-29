
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SimilarItems {
	
    static HashMap<Integer, Double> sim = new HashMap<Integer, Double>();
    static int movieCount;

	public static void main(String[] args) {
//		dataClean data = new dataClean();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_recommend", "root", "");
			Statement st1 = con.createStatement();
			ResultSet rs = st1.executeQuery("select count(*) from movie");
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_recommend", "root", "");
			Statement st2 = con1.createStatement();
			
			if(rs.next()){
				movieCount = rs.getInt(1);
			}
			if(rs != null){
				rs.close();
			}
			findSimilar(st1, st2);
			
			if(rs != null){
				rs.close();
			}
			if(st1 != null){
				st1.close();
			}
			if(st2 != null){
				st2.close();
			}
			if(con != null){
				con.close();
			}if(con1 != null){
				con1.close();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	
	private static void findSimilar(Statement st1, Statement st2) {
		
		double similarityWith = 0;
		
		try {
			st1.executeUpdate("create table if not exists similarity ("
					+ "movie_id int not null,"
					+ "sim_movie_id int not null,"
					+ "sim double not null )");
			
			int simMovieCount = 0;
			for(int mi = 1321; mi <= movieCount; mi++){
				st2.executeUpdate("start transaction;");
				for(int mj = 1; mj <= movieCount; mj++){
					if(mi != mj){
						similarityWith = similarity(mi, mj, st1, st2);
						st1.executeUpdate("insert into similarity (movie_id, sim_movie_id, sim) values "
								+ "("+mi+", "+mj+", "+similarityWith+")");
					}
				}
				st2.executeUpdate("commit;");
				ResultSet topSimilar = st1.executeQuery("select * from similarity where movie_id = "+mi+" order by sim desc");
				
				// store top 5 most similar movies to each movie
				while (topSimilar.next() && simMovieCount < 5) {
					st2.executeUpdate("insert into similar (mid, simid, similarity) values (" +topSimilar.getInt("movie_id")+", "
							+topSimilar.getInt("sim_movie_id")+", " +topSimilar.getDouble("sim")+")");
					simMovieCount++;
				}
//				reset the counter
				simMovieCount = 0;
				
//				clear the temp table similarity
				st2.executeUpdate("delete from similarity");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static double similarity(int movie1, int movie2, Statement st1, Statement st2) {
		int sumI = 0, sumJ = 0, sqI = 0, sqJ = 0, sumProd = 0, commonUsers = 0, ratingUser1 = 0, ratingUser2 = 0;
		double num = 0, den = 0;
		try {
			
			ResultSet ratings1 = st1.executeQuery("select ratings from rating use index (in_m_id) where movie_id = " + movie1
					+ " and user_id in (select user_id  from rating use index (in_m_id, in_u_id) where movie_id = "+ movie2 +")");
			
			ResultSet ratings2 = st2.executeQuery("select ratings from rating use index (in_m_id) where movie_id = " + movie2
					+ " and user_id in (select user_id  from rating use index (in_m_id, in_u_id) where movie_id = "+ movie1 +")");
			
			while(ratings1.next() && ratings2.next()){
				
				ratingUser1 += ratings1.getInt("ratings");
				ratingUser2 += ratings2.getInt("ratings");
				
				sumI += ratingUser1;
				sumJ += ratingUser2;
				
				sqI += Math.pow(ratingUser1, 2);
				sqJ += Math.pow(ratingUser2, 2);
				
				sumProd += (ratingUser1 * ratingUser2);

				commonUsers++;
			}
			
			if(ratings1 != null){
				ratings1.close();
			}
			if(ratings2 != null){
				ratings2.close();
			}
			
//			no common users that rated the two movies under consideration
			if(commonUsers == 0){
				return 0;
			}
			num = sumProd - (sumI * sumJ / commonUsers);
			den = Math.sqrt((sqI-Math.pow(sumI, 2)/commonUsers)*(sqJ-Math.pow(sumJ, 2)/commonUsers));

		}catch (NumberFormatException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		if(den == 0){
			return den;
		}
		double similarity = num / den;
		return similarity;	
	}
}
