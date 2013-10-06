package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ProductDao;

public class DelServlet extends HttpServlet {
	private static final long serialVersionUID = 7429102222559885136L;
	private ProductDao productDao = ProductDao.getInstance();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		if (null != id) {
			productDao.deleteById(id);
		}
		response.sendRedirect("home.do");
	}
}
