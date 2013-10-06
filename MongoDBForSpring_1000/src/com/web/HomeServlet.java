package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ProductDao;

public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = -4352358301393834922L;
	private ProductDao productDao = ProductDao.getInstance();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		if (null != page && limit != null) {
			request.setAttribute(
					"ps",
					productDao.getAll(Integer.parseInt(page),
							Integer.parseInt(limit)));
		} else {
			request.setAttribute("ps", productDao.getAll());
		}
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
