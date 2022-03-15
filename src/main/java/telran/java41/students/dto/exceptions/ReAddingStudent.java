package telran.java41.students.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@NoArgsConstructor
public class ReAddingStudent extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1832874519690084544L;
	
	public ReAddingStudent(int id) {
		super("student with id = " + id + ", already added");
	}

}
