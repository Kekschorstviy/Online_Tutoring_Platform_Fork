package de.thu.thutorium.database.dbObjects;

import de.thu.thutorium.database.dbObjects.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.*;

/**
 * Represents a user account entity within the system. This entity is mapped to the "user_account"
 * table in the database.
 *
 * <p>Includes basic user information such as first name, last name, role, verification status, and
 * the account creation timestamp.
 *
 * <p>Lombok annotations are used to automatically generate boilerplate code like getters, setters,
 * and constructors.
 */
@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDBO {
  /**
   * The unique identifier for the user. This value is automatically generated by the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  @Setter(AccessLevel.NONE)
  private Long userId;

  /** The first name of the user. This field is mandatory and cannot be null. */
  @Column(name = "first_name", nullable = false)
  private String firstName;

  /** The last name of the user. This field is mandatory and cannot be null. */
  @Column(name = "last_name", nullable = false)
  private String lastName;

  /** The user's email, used for login. This field must be unique. */
  @Column(name = "email_address", nullable = false, unique = true)
  private String email;

  /** The hashed password for authentication. This field is mandatory. */
  @Column(name = "hashed_password", nullable = false)
  private String password;

  /**
   * The roles of the user within the system, such as STUDENT, TUTOR etc. Multiple roles are foreseen:
    * a tutor could also be a student,
    * an admin could be a verifier.
   * The user roles are resolved as having many-to-many relations with the user.
   * The counterpart is a Set<UserDBO> called 'users' in {@link RoleDBO}
   */
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<RoleDBO> roles;

  /**
   * Defines a many-to-one relationship between a user and their affiliation with respect to the university.
   * <p>
   * This relationship is mapped by the {@code affiliation_id} foreign key column in the {@link UserDBO} entity.
   * The {@code affiliation} field represents the affiliation to which the user belongs.
   * The counterpart is denoted by a List<UserDBO> called 'affiliatedUsers' in the {@link AffiliationDBO}.
   */
  @ManyToOne
  @JoinColumn(name="affiliation_id")
  private AffiliationDBO affiliation;

  /**
   * A textual description of the user.
   */
  @Column(name="user_description", columnDefinition = "TEXT")
  private String description;

  /**
   * The timestamp when the user account was created. This field is initialised with current time.
   */
  @Column(name = "created_at")
  private LocalDateTime createdAt= LocalDateTime.now();


  /** Indicates whether the user's email is verified. Defaults to {@code false} if not specified. */
  @Column(name = "is_verified")
  private Boolean isVerified = false;

  /**
   * The timestamp when the user account was verified.
   */
  @Column(name = "verified_on")
  private LocalDateTime verified_on;


  /**
   * Verifiers for this user.
   * <p>
   * Defines a many-to-many relationship with {@link UserDBO} using the join table "users_verifiers" for defining the
   * verifiers who can verify other users. It is s uni-directional relationship(!!), since the users do not know who has
   * verified them.
   */
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(name = "users_verifiers",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "verifier_id")
  )
  private Set<UserDBO> verifiers;

  /**
   * Describes if the user is enabled or not. Default value: True.
   */
  @Column(name = "enabled")
  private Boolean enabled= true;

  /**
   * Represents the list of courses associated with this user if they are a tutor.
   * <p>This relationship is mapped by the {@code tutor} field in the {@link CourseDBO} entity. The
   * cascade type {@code CascadeType.ALL} ensures that all operations (such as persist and remove)
   * are propagated to the associated courses.
   * <p>If this user is deleted, all their associated courses will also be deleted due to the
   * cascading operations defined in this relationship.
   * The counterpart is denoted by a Set<UserDBO> called 'participants' in the {@link CourseDBO}.
   */
  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(name = "course_participants",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private Set<CourseDBO> courses;

  /**
   * Ratings given by a student to tutors.
   *<p> Defines a one-to-many relationship with {@link RatingTutorDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the student.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RatingTutorDBO> givenTutorRatings;

  /**
   * Ratings received by a tutor from students.
   * <p> Defines a one-to-many relationship with {@link RatingTutorDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the tutor.
   */
  @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RatingTutorDBO> receivedTutorRatings;

  /**
   * Ratings given by this student to courses.
   *<p> Defines a one-to-many relationship with {@link RatingCourseDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the student.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RatingCourseDBO> givenCourseRatings;

  /**
   * Meetings scheduled for the users.
   * <p>
   * Defines a many-to-many relationship with {@link UserDBO} using the join table "users_meetings" for defining the
   * meetings scheduled for the users. It is s bidirectional relationship(!!), the counterpart denoted as Set<UserDBO>
   * called 'participants' in {@link MeetingDBO}.
   */
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(name = "users_meetings",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "meeting_id")
  )
  private Set<MeetingDBO> meetings;

  /**
   * Messages sent by a sender to receiver.
   *<p> Defines a one-to-many relationship with {@link MessageDBO}.
   * The {@code orphanRemoval} attribute is set to 'false' to archive the messages.
   */
  @OneToMany(mappedBy = "sender", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
          orphanRemoval = false)
  private List<MessageDBO> messages_sent;

  /**
   * Messages received by a receiver from a sender.
   * <p> Defines a one-to-many relationship with {@link MessageDBO}.
   * The {@code orphanRemoval} attribute is set to 'false' to archive the messages.
   */
  @OneToMany(mappedBy = "receiver", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
          orphanRemoval = false)
  private List<MessageDBO> messages_received;


  /**
   * Ratings given by a student to tutors.
   *<p> Defines a one-to-many relationship with {@link RatingTutorDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the student.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProgressDBO> receivedScores;

}