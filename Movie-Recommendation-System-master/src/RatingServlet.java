

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RatingServlet
 */
@WebServlet("/RatingServlet")
public class RatingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RatingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uid = request.getParameter("userid");
		String movie = request.getParameter("mname");
		String rating = request.getParameter("rating");
		System.out.println(movie+rating);
		int r = Integer.parseInt(rating);
		int id = Integer.parseInt(uid);
		
		Recommend rec = new Recommend();
		rec.addMovie(movie, r, id);
		System.out.println("Done inserting");
		//response.sendRedirect("webpage.jsp");
/*		   response.getOutputStream().println("<script type=\"text/javascript\">");
		   response.getOutputStream().println("alert('Inserted Rating Successfully!');");
		   response.getOutputStream().println("</script>")*/;
		   
			Recommend rec2 = new Recommend();
			ArrayList<String> toprated = rec2.topRated(id);
			
			request.setAttribute("toplist", toprated);
			getServletConfig().getServletContext().getRequestDispatcher("/page2.jsp").forward(request,response);
		   //response.sendRedirect("addmovie.jsp");
		
	}

}
