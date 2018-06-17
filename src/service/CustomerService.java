package service;

import java.sql.ResultSet;
import java.util.List;

import dao.CustomerDao;
import model.Customer;

public class CustomerService {

	private CustomerDao customerDao;
	
	public CustomerService() {
		customerDao = new CustomerDao();
	}
	
	public int add(Customer c) {
		
		return customerDao.add(c);
	}
	
	public List<Customer> getAll() {
		return customerDao.getAll();
	}
	
	public Customer getCustomer(String id) {
		return customerDao.getCustomer(id);
	}
	
	public boolean delete(String id) {
		return customerDao.delete(id);
	}
	
	public boolean update(Customer c) {
		return customerDao.update(c);
	}
}
