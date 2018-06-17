package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Customer;
import service.CustomerService;

/**
 * Servlet implementation class CustomerServlet
 */
@WebServlet("/CustomerServlet/*")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Gson gson;   
    private CustomerService customerService;
    
    private static final String JSON_OK = "{\"status\":\"ok\"}";
    private static final String JSON_FAIL = "{\"status\":\"fail\"}";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerServlet() {
        super();
        gson = new Gson();
        customerService = new CustomerService();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// See if get request has id parameter, if so return single customer,
		// otherwise return all
		String path = request.getPathInfo();
		PrintWriter out = response.getWriter();
		response.setContentType("text/json;charset=utf-8");
		if (path == null) {
			List<Customer> customers = customerService.getAll();
			String data = gson.toJson(customers);
			out.println(data);
			out.close();
		} else {
			String id = extractId(request.getPathInfo());
			Customer customer = customerService.getCustomer(id);
			if (customer == null) {
				out.println(JSON_FAIL);
			} else {
				out.println(gson.toJson(customer));
			}
		} 
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Customer c = getCustomer(request);
		// Execute insert operation
		int result = customerService.add(c);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		if (result == 1) {
			out.println(JSON_OK);
		} else {
			out.println(JSON_FAIL);
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = extractId(request.getPathInfo());
		Customer c = getCustomer(request);
		c.setId(id);
		boolean ret = customerService.update(c);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		if (ret) {
			out.println(JSON_OK);
		} else {
			out.println(JSON_FAIL);
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get customer id which is going to be deleted 
		String id = extractId(request.getPathInfo());
		boolean result = customerService.delete(id);
		PrintWriter out = response.getWriter();
		if (result) {
			out.println(JSON_OK);
		} else {
			out.println(JSON_FAIL);
		}
		out.close();
	}
	
	private String extractId(String path) {
		String[] params = path.split("/");
		String id = params[1];
		return id;
	}
	
	private Customer getCustomer(HttpServletRequest request) {
		// Get json from the request and construct the Customer object using gson
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader reader;
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		Customer c = gson.fromJson(sb.toString(), Customer.class);
		return c;
	}

}
