package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;

import DAO.ApplyDAO;
import DAO.PostDAO;
import model.Account;
import model.Apply;

/**
 * Servlet implementation class DeleteApplyController
 */
@WebServlet(name = "delete-apply", urlPatterns = { "/employee/delete-apply", "/employer/delete-apply"})
public class DeleteApplyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteApplyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		HttpSession Session = request.getSession();
		Account acc = (Account) Session.getAttribute("acc");
		String idString=request.getParameter("postId");
		System.out.println(idString);
	
		ObjectId postId = new ObjectId(idString);
		String accountIdString=request.getParameter("accountId");
		ObjectId accountId=new ObjectId();
		MongoClient mongo = (MongoClient) request.getServletContext().getAttribute("MONGODB_CLIENT");
		try {
			if (accountIdString == null)
				accountId = acc.getId();
			else {
				accountId=new ObjectId(accountIdString);
			}
		} catch (Exception e) {
			response.sendRedirect(request.getHeader("referer"));
			return;
		}
		ApplyDAO applyDAO = new ApplyDAO(mongo);
		if(applyDAO.DeletePostSelectedPost(accountId, postId)) {
			PostDAO postDAO = new PostDAO(mongo);
			postDAO.updatePostSubApply(postId);
		}
		
		request.setAttribute("msg", "Xoá thành công");
		response.sendRedirect(request.getHeader("referer"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
