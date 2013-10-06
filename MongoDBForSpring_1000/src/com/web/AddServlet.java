package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ProductDao;
import com.entity.Product;
import com.util.Constant;

public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 7079351252089822871L;
	private ProductDao productDao = ProductDao.getInstance();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding(Constant.utf8);
		response.setCharacterEncoding(Constant.utf8);
		Product p = new Product();
		p.setName(request.getParameter("name"));
		p.setPrice(99.99);
		p.setStatus(0);
		productDao.add(p);
		response.sendRedirect("home.do");
	}
}
