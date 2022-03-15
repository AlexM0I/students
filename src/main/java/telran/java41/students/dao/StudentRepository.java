package telran.java41.students.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.java41.students.dto.StudentDto;
import telran.java41.students.model.Student;

public interface StudentRepository extends MongoRepository<Student, Integer> {
	
	List<StudentDto> findByNameIgnoreCase(String name);

	long countStudentByNameInIgnoreCase(List<String> names);
	
	@Query("{'scores.Math': {'$gte': 85}}")
	Stream<Student> findeByExamAndScoreGreateEqualsThan(String exam, int score);

}
