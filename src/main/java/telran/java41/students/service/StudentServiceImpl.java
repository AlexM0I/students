package telran.java41.students.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import telran.java41.students.dao.StudentRepository;
import telran.java41.students.dto.ScoreDto;
import telran.java41.students.dto.StudentCredentialsDto;
import telran.java41.students.dto.StudentDto;
import telran.java41.students.dto.UpdateStudentDto;
import telran.java41.students.dto.exceptions.ReAddingStudent;
import telran.java41.students.dto.exceptions.StudentNotFoundException;
import telran.java41.students.model.Student;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

	StudentRepository studentRepository;
	ModelMapper modelMapper;

	@Override
	public boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
		Student student = studentRepository.findById(studentCredentialsDto.getId()).orElse(null);
		if (student == null) {
			student = new Student(studentCredentialsDto.getId(), studentCredentialsDto.getName(),
					studentCredentialsDto.getPassword());
			studentRepository.save(student);
			return true;
		}
		throw (new ReAddingStudent(student.getId()));
	}

	@Override
	public StudentDto findStudent(Integer id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
		return modelMapper.map(student, StudentDto.class);
	}

	@Override
	public StudentDto deleteStudent(Integer id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
		studentRepository.deleteById(id);
		return StudentDto.builder().id(id).name(student.getName()).scores(student.getScores()).build();
	}

	@Override
	public StudentCredentialsDto updateStudent(Integer id, UpdateStudentDto updateStudentDto) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
		if (updateStudentDto.getName() != null) {
			student.setName(updateStudentDto.getName());
		}
		if (updateStudentDto.getPassword() != null) {
			student.setPassword(updateStudentDto.getPassword());
		}
		studentRepository.save(student);
		return StudentCredentialsDto.builder().id(id).name(student.getName()).password(student.getPassword()).build();
	}

	@Override
	public boolean addScore(Integer id, ScoreDto scoreDto) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
		boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		studentRepository.save(student);
		return res;

	}

	@Override
	public List<StudentDto> findStudentsByName(String name) {
		List<StudentDto> res = studentRepository.findByNameIgnoreCase(name);
		if (!res.isEmpty()) {
			return res;
		}
		throw (new StudentNotFoundException());
	}

	@Override
	public long getStudentsNamesQuantity(List<String> names) {
		return studentRepository.countStudentByNameInIgnoreCase(names);
	}

	@Override
	public List<StudentDto> getStudentsByExamScore(String exam, int score) {
		return studentRepository.findAll().stream()
				.filter(s -> s.getScores().containsKey(exam) && s.getScores().get(exam) > score)
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores())).collect(Collectors.toList());
	}

}
