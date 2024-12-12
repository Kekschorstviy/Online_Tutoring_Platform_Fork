package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.frontendMappers.UserMapper;
import de.thu.thutorium.api.transferObjects.UserBaseDTO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link UserService} interface that provides methods for retrieving and
 * managing user data, including retrieving counts of students and tutors, and fetching user details
 * by their unique identifiers.
 *
 * <p>This service interacts with the {@link UserRepository} to retrieve user data and utilizes
 * {@link UserMapper} to map data between {@link UserDBO} and {@link UserBaseDTO}.
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private UserMapper userMapper;

  /**
   * Returns the total number of students in the system.
   *
   * <p>This method queries the {@link UserRepository} to count all users with the role "STUDENT".
   *
   * @return the total number of students as a {@code Long}.
   */
  @Override
  public Long getStudentCount() {
    return userRepository.countByRole("STUDENT");
  }

  /**
   * Returns the total number of tutors in the system.
   *
   * <p>This method queries the {@link UserRepository} to count all users with the role "TUTOR".
   *
   * @return the total number of tutors as a {@code Long}.
   */
  @Override
  public Long getTutorCount() {
    return userRepository.countByRole("TUTOR");
  }

  /**
   * Finds a user by their unique user ID.
   *
   * <p>This method fetches the {@link UserDBO} object from the {@link UserRepository} using the
   * provided {@code userId} and maps it to a {@link UserBaseDTO} using the {@link UserMapper}. If
   * no user is found, {@code null} is returned.
   *
   * @param userId the unique ID of the user to retrieve.
   * @return a {@link UserBaseDTO} representing the user, or {@code null} if no user is found.
   */
  @Override
  public UserBaseDTO findByUserId(Long userId) {
    // Fetch UserDBO from the repository
    UserDBO user = userRepository.findByUserId(userId);

    if (user != null) {
      // Map UserDBO to UserBaseDTO
      return userMapper.toDTO(user);
    }
    return null;
  }

  /**
   * Finds a tutor by their unique tutor ID.
   *
   * <p>This method fetches the {@link UserDBO} object from the {@link UserRepository} using the
   * provided {@code tutorId} and maps it to a {@link UserBaseDTO} using the {@link UserMapper}. If
   * no tutor is found, {@code null} is returned.
   *
   * @param tutorId the unique ID of the tutor to retrieve.
   * @return a {@link UserBaseDTO} representing the tutor, or {@code null} if no tutor is found.
   */
  @Override
  public UserBaseDTO getTutorByID(Long tutorId) {
    UserDBO user = userRepository.findByTutorId(tutorId);

    if (user != null) {
      return userMapper.toDTO(user);
    }
    return null;
  }
}