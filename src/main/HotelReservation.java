package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservation {

	private static final String url = "jdbc:mysql://localhost:3306/JDBC_hotel_DB";
	private static final String user = "root";
	private static final String password = "root";

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		Connection connection = DriverManager.getConnection(url, user, password);
		System.out.println("Connected to databases successfully...");
		System.out.println();

		Scanner sc = new Scanner(System.in);
		boolean exit = false;

		do {
			System.out.println();
			System.out.println("Enter the choise");
			System.out.println("-------------------");
			System.out.println("1.(C)	Do Reservation");
			System.out.println("2.(R)	View all Reservation");
			System.out.println("3.(U)	Update Reservation");
			System.out.println("4.(D)	Delete Reservation");
			System.out.println("0.Exit");
			System.out.println("=========X=========");

			int choise = sc.nextInt();

			switch (choise) {
			case 1: {
				System.out.println("In reservation section");
				doReservation(connection, sc);
				break;

			}

			case 2: {
				System.out.println("In view all reservation table");
				viewReservations(connection, sc);
				break;
			}

			case 3: {
				updateReservation(connection, sc);
				break;
			}

			case 4: {
				System.out.println("Enter reservationId");
				int reservationId=sc.nextInt();
				deleteReservation(connection, reservationId);
				break;
			}

			case 0: {
				connection.close();
				sc.close();
				System.out.println("Connection closed and Program ended....");
				exit = true;
				break;
			}
			default:
				System.out.println("Enter valid choise");
				break;
			}

		} while (!exit);

	}

	private static void doReservation(Connection connection, Scanner sc) {
		System.out.println("Enter the guest name");
		String guest_name = sc.next();
		sc.nextLine();
		System.out.println("Enter the room number");
		int room_id = sc.nextInt();
		System.out.println("Enter the contact number");
		String contact_no = sc.next();

		String query = "insert into reservations (guest_name,room_id,contact_no) values ('" + guest_name + "',"
				+ room_id + ",'" + contact_no + "');";
		try {
			Statement statement = connection.createStatement();
			int affectedrows = statement.executeUpdate(query);

			if (affectedrows > 0) {
				System.out.println("Reservation is successful, affected rows are:- " + affectedrows);
			} else {
				System.out.println("Reservation is failed");
				return;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void viewReservations(Connection connection, Scanner sc) {
		System.out.println("This are all the reservations: ");
		System.out.println(
				"+-----------------+----------------+---------------+------------------+--------------------------+");
		System.out.println(
				"|  Reservation id |   Guest Name   |  Room Numbeer |     Contact no   |     Reservation Date     |");
		System.out.println(
				"+-----------------+----------------+---------------+------------------+--------------------------+");

		try {
			Statement statement = connection.createStatement();
			String query = "Select * from reservations";
			ResultSet resultset = statement.executeQuery(query);

			while (resultset.next()) {
				int id = resultset.getInt("reservationId");
				String guest = resultset.getString("guest_Name");
				int room_id = resultset.getInt("room_id");
				String contact_no = resultset.getString("contact_no");
				String date = resultset.getTimestamp("reservation_date").toString();

				System.out.printf("|%-17d|%16s|%15s|%18s|%26s| \n", id, guest, room_id, contact_no, date);

			}
			System.out.println(
					"+-----------------+----------------+---------------+------------------+--------------------------+");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void updateReservation(Connection connection, Scanner sc) {
		System.out.println("Enter the reservation id");
		int reservationId = sc.nextInt();
		if (reservationExistOrNot(connection, reservationId)) {
			System.out.println("Resevation id found");
			System.out.println("Enter the details to update");
			System.out.println("Enter the guest name");
			String guest_name = sc.next();
			sc.nextLine();
			System.out.println("Enter the room number");
			int room_id = sc.nextInt();
			System.out.println("Enter the contact number");
			String contact_no = sc.next();

			String query = "update reservations set guest_name='" + guest_name + "',room_id=" + room_id
					+ ",contact_no= '" + contact_no + "' where reservationId=" + reservationId + ";";

			try {
				Statement statement = connection.createStatement();
				int affectedrow = statement.executeUpdate(query);

				if (affectedrow > 0) {
					System.out.println("Update successfully...");
				} else {
					System.out.println("update failed....");
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} else {
			System.out.println("invalid Reservation Id, try again");
			return;
		}

	}

	private static void deleteReservation(Connection connection, int reservationId) {
		if(reservationExistOrNot(connection, reservationId)) {
			
			String query="delete from reservations where reservationId="+reservationId;
			
			try {
				Statement statement=connection.createStatement();
				int affectedrows=statement.executeUpdate(query);
				if(affectedrows>0) {
					System.out.println("Deleted Successfully");
				}else
					System.out.println("Failed to delete..");
				
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			
		}else {
			System.out.println("Reservation Id not found...");
			return;
		}
	}
	
	private static boolean reservationExistOrNot(Connection connection, int reservationId) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultset = statement
					.executeQuery("select * from reservations where reservationId=" + reservationId + ";");
			if (resultset.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	
}
