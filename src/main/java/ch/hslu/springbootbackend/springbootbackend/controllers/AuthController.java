package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamSetService;
import ch.hslu.springbootbackend.springbootbackend.payload.request.LoginRequest;
import ch.hslu.springbootbackend.springbootbackend.payload.request.SignupRequest;
import ch.hslu.springbootbackend.springbootbackend.payload.response.JwtResponse;
import ch.hslu.springbootbackend.springbootbackend.payload.response.MessageResponse;
import ch.hslu.springbootbackend.springbootbackend.security.jwt.JwtUtils;
import ch.hslu.springbootbackend.springbootbackend.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	private final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
    AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ExamSetRepository examSetRepository;

	@Autowired
	SchoolClassRepository schoolClassRepository;

	@Autowired
	ExamSetService examSetService;

	@Autowired
	ProfessionRepository professionRepository;

	@Autowired
    PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		if(roles.contains("ROLE_EXAM")){
			User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
			userRepository.save(removeExamRole(user));
		}

		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/startExam")
	public ResponseEntity<?> authenticateUserForExam(@Valid @RequestBody LoginRequest loginRequest, @RequestParam int examSetId) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new RuntimeException("Error: User is not found."));;
		String jwt;
		long expirationTime;
		if (checkIfUserHasPermissionToStartExam(user, examSetId)) {
			user = addExamRole(user);
			expirationTime = examSetService.getTimeForExam(examSetId);
			jwt = jwtUtils.generateJwtToken(authentication, expirationTime);
		}else{
			jwt = jwtUtils.generateJwtToken(authentication);
			return ResponseEntity.
					badRequest()
					.build();
		}


		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	@PreAuthorize("hasRole('ROLE_EXAM')")
	@PostMapping("/endExam")
	public ResponseEntity<?> endExam(@RequestParam int examSetId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("Error: User is not found."));;
		String jwt;
		user = removeExamRole(user);
		jwt = jwtUtils.generateJwtToken(auth);


		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user account
		User user = new User(signUpRequest.getUsername(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));
		LOG.warn(String.valueOf(signUpRequest.getProfessionId()));
		Profession profession = professionRepository.findById(signUpRequest.getProfessionId()).orElse(null);
		if(profession != null) {
			user.setProfession(profession);
		}
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "exam":
					Role examRole = roleRepository.findByName(ERole.ROLE_EXAM)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(examRole);

					break;
				case "teacher":
					Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity
				.created(URI.create(user.getId().toString()))
				.contentType(MediaType.APPLICATION_JSON)
				.body(user);
	}

	private User addExamRole(User user){
		Role examRole = roleRepository.findByName(ERole.ROLE_EXAM)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));;

				user.getRoles().add(examRole);
		return userRepository.save(user);

	}
	private User removeExamRole(User user){
		Role examRole = roleRepository.findByName(ERole.ROLE_EXAM)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));;

		user.getRoles().remove(examRole);
		return userRepository.save(user);

	}

	private Boolean checkIfUserHasPermissionToStartExam(User user, int examSetId){
		List<SchoolClass> schoolClasses = schoolClassRepository.findAllByUsersInClass(user);
		ExamSet examSet = examSetRepository.findById(examSetId).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		if(examSet.getStartDate().getTime() > new Date().getTime()){
			return false;
		}
		Boolean exists = false;
		for(SchoolClass schoolClass : schoolClasses){
			if(examSetRepository.findAllBySchoolClassesInExamSet(schoolClass).size() != 0 ){
				return true;
			}
		}
		return exists;
	}



}
