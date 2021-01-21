package com.hcl.NewFoodProduct;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FoodProduct extends HttpServlet {
	String url = "jdbc:mysql://localhost:3306/Foods?createDatabaseIfNotExist=true";
	String user = "root";
	String password = "root";
	String sqlQuery;
	String findIDQuery;
	Connection myConnection = null;

	public FoodProductDTO findID(int id) {
		FoodProductDTO fpd = null;
		findIDQuery = "SELECT ProductName FROM FoodProduct WHERE ProductID = ?";
		System.out.println(findIDQuery);
		try (PreparedStatement myPreparedStatement = myConnection.prepareStatement(findIDQuery)) {
			myPreparedStatement.setInt(1, id);
			ResultSet rs = myPreparedStatement.executeQuery();
			System.out.println(rs);
			while (rs.next()) {
				String name = rs.getString("ProductName");
				fpd = new FoodProductDTO(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fpd;
	}

	public void init() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			myConnection = DriverManager.getConnection(url, user, password);
			System.out.println("Connection Successful");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Connection Not Successful");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<form action='' method='POST'>");
		out.println("<label>Enter Product ID: <input type='text' name='product-id'></input></label>");
		out.println("<input type='submit'>Get Product Name</input>");
		out.println("</form>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productId = request.getParameter("product-id");
		FoodProductDTO fpd = findID(Integer.parseInt(productId));
		PrintWriter out = response.getWriter();
		if (fpd == null) {
			out.println("Record Not Found");
		} else {
			out.println("Your Product Name Is " + fpd.getName());
		}

	}
}
