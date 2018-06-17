package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import model.Customer;


public class CustomerDao {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/customer?serverTimezone=UTC";
    
    static final String USER = "root";
    static final String PASS = "86660896";
    
    static final String SQL_ADD = "INSERT INTO t_customer VALUES (?, ?, ?, ?, ?, ?)";
    static final String SQL_UPDATE = "UPDATE t_customer SET name=?,gender=?,phone=?,email=?,description=? where id=?";
    static final String SQL_GETALL = "SELECT * FROM t_customer";
    static final String SQL_GET = "SELECT * FROM t_customer WHERE id=?";
    static final String SQL_DELETE = "DELETE FROM t_customer WHERE id=?";
    
    Connection conn = null;
    PreparedStatement stmt = null;
    QueryRunner queryRunner = null;
    
	public CustomerDao() {
		try {
			queryRunner = new QueryRunner();
			DbUtils.loadDriver(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int add(Customer c) {
		try {
			UUID uuid = UUID.randomUUID();
			int ret = queryRunner.update(conn, SQL_ADD, 
							uuid.toString().substring(0, 32),
							c.getName(),
							c.getGender(),
							c.getPhone(),
							c.getEmail(),
							c.getDescription());
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
	public List<Customer> getAll() {
		ResultSetHandler<List<Customer>> resultHandler = 
				new BeanListHandler<Customer>(Customer.class);
		try {
			List<Customer> customers = queryRunner.query(conn, SQL_GETALL, resultHandler);
			return customers;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Customer getCustomer(String id) {
		ResultSetHandler<Customer> resultHandler =
				new BeanHandler<Customer>(Customer.class);
		try {
			Customer customer = queryRunner.query(conn, SQL_GET, resultHandler, id);
			System.out.println("id: " + id + " customer:" + customer);
			return customer;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean update(Customer c) {
		try {
			int ret = queryRunner.update(conn, SQL_UPDATE, 
					c.getName(),
					c.getGender(),
					c.getPhone(),
					c.getEmail(),
					c.getDescription(),
					c.getId());
			if (ret == 1) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(String id) {
		try {
			int deletedRecords = queryRunner.update(conn, SQL_DELETE, id);
			System.out.println(deletedRecords + " record(s) deleted.");
			if (deletedRecords == 1) {
				return true;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
