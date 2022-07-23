package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
class Token{
	private String token;
	Token(){}
	Token(String token){
		this.token = token;
	}
	public String getToken(){ return token; }
	public void setToken(String value){ this.token = value; }
}
class Seat {
	@JsonIgnore
	private String token;
	private int row;
	private int column;
	private int price;
	@JsonIgnore
	private boolean isPurchased = false;

	public Seat(){

	}
	public Seat(int row, int column) {
		this.row = row;
		this.column = column;
		if(this.row <= 4)
			this.price = 10;
		else this.price = 8;
		token = UUID.randomUUID().toString();;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	public int getPrice(){ return price; }
	public void setPrice(int price) { this.price = price; }
	public void setPurchased(boolean status){ isPurchased = status; }
	@JsonIgnore
	public boolean getPurchased(){ return isPurchased; }
	@JsonIgnore
	public String getToken(){ return token; }
	public void setToken(String token){ this.token = token; }
}
class MovieTheatre {
	@JsonIgnore
	private int currentIncome;
	@JsonIgnore
	private int number_of_available_seats;
	@JsonIgnore
	private int number_of_purchased_tickets;
	private int total_rows;
	private int total_columns;
	private List<Seat> total_seats;

	public MovieTheatre() {}

	public MovieTheatre(int total_rows, int total_columns) {
		this.total_rows = total_rows;
		this.total_columns = total_columns;
		this.total_seats = assembleSeats();
		this.currentIncome = 0;
		this.number_of_available_seats = total_seats.size();
		number_of_purchased_tickets = 0;
	}

	public int getTotal_rows() {
		return total_rows;
	}

	public void setTotal_rows(int total_rows) {
		this.total_rows = total_rows;
	}

	public int getTotal_columns() {
		return total_columns;
	}

	public void setTotal_columns(int total_columns) {
		this.total_columns = total_columns;
	}
	@JsonIgnore
	public List<Seat> getTotal_seats(){
		return total_seats;
	}

	public List<Seat> getAvailable_seats() {
		List<Seat> currentAvailableSeats = filterSeats();
		return currentAvailableSeats;
	}
	public List<Seat> filterSeats(){
		List<Seat> temporary = new ArrayList<>();
		for(int i=0;i<total_seats.size();i++)
			if(!total_seats.get(i).getPurchased())
				temporary.add(total_seats.get(i));
		return temporary;
	}

	public void setAvailable_seats(List<Seat> available_seats) {
		this.total_seats = available_seats;
	}
	public Seat getSeatByIndex(int index){ return total_seats.get(index); }
	public List<Seat> assembleSeats() {
		List<Seat> totalSeats = new ArrayList<>();
		for (int i = 1; i <= total_rows; i++) {
			for (int j = 1; j <= total_columns; j++) {
				totalSeats.add(new Seat(i, j));
			}
		}
		return totalSeats;
	}
	@JsonIgnore
	public int getCurrentIncome(){
		return currentIncome;
	}
	public void setCurrentIncome(int currentIncome){
		this.currentIncome = currentIncome;
	}
	@JsonIgnore
	public int getNumber_of_available_seats(){
		return number_of_available_seats;
	}
	public void setNumber_of_available_seats(int number_of_available_seats){
		this.number_of_available_seats = number_of_available_seats;
	}
	@JsonIgnore
	public int getNumber_of_purchased_tickets(){
		return number_of_purchased_tickets;
	}
	public void setNumber_of_purchased_tickets(int number_of_purchased_tickets){
		this.number_of_purchased_tickets = number_of_purchased_tickets;
	}
}

@RestController
class TheatreController {

	MovieTheatre theatre = new MovieTheatre(9, 9);

	@GetMapping("/seats")
	public MovieTheatre getAvailableSeats() {
		return theatre;
	}

	@PostMapping("/purchase")
	public ResponseEntity purchaseSeat(@RequestBody Seat data) {
		int row = data.getRow();
		int column = data.getColumn();
		{
			if (row > 9 || row < 1 || column > 9 || column < 1)
				return new ResponseEntity(Map.of("error","The number of a row or a column is out of bounds!"),HttpStatus.BAD_REQUEST);
			for (Seat seat : theatre.getAvailable_seats()) {
				if (seat.getRow() == row && seat.getColumn() == column) {
					if (!seat.getPurchased()) {
						seat.setPurchased(true);
						theatre.setCurrentIncome(theatre.getCurrentIncome()+seat.getPrice());
						theatre.setNumber_of_available_seats(theatre.getNumber_of_available_seats()-1);
						theatre.setNumber_of_purchased_tickets(theatre.getNumber_of_purchased_tickets()+1);
						return new ResponseEntity(Map.of
								("token",seat.getToken(),
										"ticket",seat),HttpStatus.OK);
					}
				}
			}
			return new ResponseEntity(Map.of("error","The ticket has been already purchased!"),HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/return")
	public ResponseEntity returnSeat(@RequestBody Token token){
		for(Seat seat : theatre.getTotal_seats()){
			if(seat.getToken().equals(token.getToken())){
				seat.setPurchased(false);
				theatre.setCurrentIncome(theatre.getCurrentIncome()-seat.getPrice());
				theatre.setNumber_of_available_seats(theatre.getNumber_of_available_seats()+1);
				theatre.setNumber_of_purchased_tickets(theatre.getNumber_of_purchased_tickets()-1);
				return new ResponseEntity(Map.of("returned_ticket", seat),HttpStatus.OK);
			}
		}
		return new ResponseEntity(Map.of("error","Wrong token!"),HttpStatus.BAD_REQUEST);
	}
	@PostMapping("/stats")
	public ResponseEntity getStats(@RequestParam(required = false) String password){
		try {
			if (password.equals("super_secret")) {
				return new ResponseEntity(Map.of("current_income", theatre.getCurrentIncome(),
						"number_of_available_seats", theatre.getNumber_of_available_seats(),
						"number_of_purchased_tickets", theatre.getNumber_of_purchased_tickets())
						, HttpStatus.OK);
			}
		}catch(Exception exception){}
		return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
	}
}