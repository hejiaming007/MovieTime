package com.jimmystudio.movietime_service.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.h2.engine.Constants;
import org.h2.tools.Server;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.jimmystudio.movietime_service.entity.Comment;
import com.jimmystudio.movietime_service.entity.Movie;
import com.jimmystudio.movietime_service.entity.User;
import com.jimmystudio.movietime_service.entity.UserGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RepositoryTest {

	private static int counter = 0;

	private static final Date MOVIE_CREATION_DATE = new DateTime().plus(
			counter++).toDate();

	private static final String MOVIE_NAME = "Titanic";
	private static final String MOVIE_DESCRIPTION_1 = "Good movie!";
	private static final String MOVIE_DESCRIPTION_2 = "Very goold movie";

	private static final String USER1_NAME = "Jack";
	private static final String USER1_PASSWORD = "password1";
	private static final Date USER1_REGISTRATION_DATE = new DateTime().plus(
			counter++).toDate();

	private static final String USER2_NAME = "David";
	private static final String USER2_PASSWORD = "password2";
	private static final Date USER2_REGISTRATION_DATE = new DateTime().plus(
			counter++).toDate();

	private static final String COMMENT1_CONTENT = "Comment1 Content";
	private static final String COMMENT2_CONTENT = "Comment2 Content";
	private static final Date COMMENT1_DATE = new DateTime().plus(counter++)
			.toDate();
	private static final Date COMMENT2_DATE = new DateTime().plus(counter++)
			.toDate();

	private static final String GROUP_ADMIN = "Admin Group";

	private static final String GROUP_ALL_USERS = "All User Group";

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	JpaRepository<Movie, Long> movieRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	JpaTransactionManager transactionManager;

	private Server server;

	@Before
	public void setup() throws Exception {
		server = Server.createWebServer();
//		server.start();
//		Server.openBrowser("http://localhost:" + Constants.DEFAULT_HTTP_PORT);
	}

	@After
	public void tearDown() {
		if(server.isRunning(true)){
			server.stop();
		}
	}

	@Test
	public void testSingleMovieRepositoryAutoCommit() {

		Assert.assertNotNull(movieRepository);
		Assert.assertNotNull(userRepository);

		Movie movie = new Movie();
		movie.setName(MOVIE_NAME);
		movie.setCreationDate(MOVIE_CREATION_DATE);
		movie.setDescription(MOVIE_DESCRIPTION_1);

		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is null but not throw Exception here.");
		} catch (JpaSystemException e) {
			// Expected here.
		}

		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is mandatory for Movie and it's not yet created before saving, and Exception not throw here.");
		} catch (Exception e) {
			// Expected here.
		}

		User user1 = new User();
		user1.setName(USER1_NAME);
		user1.setPassword(USER1_PASSWORD);
		user1.setRegistionDate(USER1_REGISTRATION_DATE);
		user1 = userRepository.save(user1);

		User user2 = new User();
		user2.setName(USER2_NAME);
		user2.setPassword(USER2_PASSWORD);
		user2.setRegistionDate(USER2_REGISTRATION_DATE);
		user2 = userRepository.save(user2);

		UserGroup userGroup1 = new UserGroup();
		userGroup1.setName(GROUP_ALL_USERS);
		userGroup1.getUsers().add(user1);
		userGroup1.getUsers().add(user2);

		UserGroup userGroup2 = new UserGroup();
		userGroup2.setName(GROUP_ADMIN);
		userGroup2.getUsers().add(user1);// Only user1 is admin

		userGroup1 = userGroupRepository.save(userGroup1);
		userGroup2 = userGroupRepository.save(userGroup2);

		Assert.assertEquals(GROUP_ALL_USERS,
				userGroupRepository.findOne(userGroup1.getId()).getName());
		Assert.assertEquals(GROUP_ADMIN,
				userGroupRepository.findOne(userGroup2.getId()).getName());

		List<UserGroup> groupList1 = new ArrayList<UserGroup>();
		groupList1.add(userGroup1);
		List<UserGroup> groupList2 = new ArrayList<UserGroup>();
		groupList2.add(userGroup2);
		Assert.assertEquals(2, userRepository.findByUserGroupsIn(groupList1)
				.size());
		Assert.assertEquals(1, userRepository.findByUserGroupsIn(groupList2)
				.size());

		List<User> userList1 = new ArrayList<User>();
		userList1.add(user1);
		List<User> userList2 = new ArrayList<User>();
		userList2.add(user2);
		Assert.assertEquals(2, userGroupRepository.findByUsersIn(userList1)
				.size());
		Assert.assertEquals(1, userGroupRepository.findByUsersIn(userList2)
				.size());

		Assert.assertEquals(2, getGroupWithUsers(userGroup1.getId()).getUsers()
				.size());
		Assert.assertEquals(1, getGroupWithUsers(userGroup2.getId()).getUsers()
				.size());

		Comment comment1 = new Comment();
		comment1.setContent(COMMENT1_CONTENT);
		comment1.setCreationDate(COMMENT1_DATE);
		comment1.setCreator(user1);

		Comment comment2 = new Comment();
		comment2.setContent(COMMENT2_CONTENT);
		comment2.setCreationDate(COMMENT2_DATE);
		comment2.setCreator(user2);

		// movie.getComments().add(comment1);
		// movie.getComments().add(comment2);

		movie.setCreator(user1);// user1 is already persited, so need call set
		// this object after persit.

		movie = movieRepository.save(movie);

		comment1.setMovie(movie);
		comment2.setMovie(movie);

		commentRepository.save(comment1);
		commentRepository.save(comment2);

		Assert.assertNotNull(movie.getId());

		movie = movieRepository.findOne(movie.getId());
		Integer version1 = movie.getVersion();
		Assert.assertNotNull(movie);
		Assert.assertEquals(MOVIE_NAME, movie.getName());
		Assert.assertEquals(MOVIE_DESCRIPTION_1, movie.getDescription());
		Assert.assertEquals(MOVIE_CREATION_DATE, movie.getCreationDate());

		Assert.assertEquals(USER1_NAME, movie.getCreator().getName());
		Assert.assertEquals(USER1_PASSWORD, movie.getCreator().getPassword());
		Assert.assertEquals(USER1_REGISTRATION_DATE, movie.getCreator()
				.getRegistionDate());

		// Trigger lazy load of movie.comments
		// movie = getMovieWithComments(movie.getId());
		// List<Comment> comments = movie.getComments();

		List<Comment> comments = commentRepository
				.findByMovieOrderByCreationDateDesc(movie);

		Assert.assertEquals(2, comments.size());
		Assert.assertEquals(COMMENT2_CONTENT, comments.get(0).getContent());
		Assert.assertEquals(COMMENT2_DATE, comments.get(0).getCreationDate());
		Assert.assertEquals(user2.getName(), comments.get(0).getCreator()
				.getName());
		// Assert.assertEquals(movie.getId(),
		// comments.get(0).getMovie().getId());

		Assert.assertEquals(COMMENT1_CONTENT, comments.get(1).getContent());
		Assert.assertEquals(COMMENT1_DATE, comments.get(1).getCreationDate());
		Assert.assertEquals(user1.getName(), comments.get(1).getCreator()
				.getName());
		// Assert.assertEquals(movie.getId(),
		// comments.get(1).getMovie().getId());

		movie.setDescription(MOVIE_DESCRIPTION_2);
		movie = movieRepository.save(movie);
		Integer version2 = movie.getVersion();
		Assert.assertEquals(MOVIE_DESCRIPTION_2, movie.getDescription());
		Assert.assertTrue(version2 > version1);

		// Delete all.
		movieRepository.deleteAll();

		Assert.assertEquals(0, movieRepository.count());

		// Remove movie should not remove user.
		Assert.assertEquals(2, userRepository.findAll().size());

		// Remove movie should remove related comments
		Assert.assertEquals(0, commentRepository.count());

	}

	/*
	 * @Transactional public void testSingleMovieRepositoryInTransaction() {
	 * 
	 * Assert.assertNotNull(movieRepository);
	 * Assert.assertNotNull(userRepository);
	 * 
	 * Movie movie = new Movie(); movie.setName(MOVIE_NAME);
	 * movie.setCreationDate(MOVIE_CREATION_DATE);
	 * movie.setDescription(MOVIE_DESCRIPTION_1);
	 * 
	 * try { movie = movieRepository.save(movie);
	 * Assert.fail("Creator is null but not throw Exception here."); } catch
	 * (JpaSystemException e) { // Expected here. }
	 * 
	 * movie = movieRepository.save(movie);
	 * 
	 * 
	 * User user1 = new User(); user1.setName(USER1_NAME);
	 * user1.setPassword(USER1_PASSWORD);
	 * user1.setRegistionDate(USER1_REGISTRATION_DATE); user1 =
	 * userRepository.save(user1);
	 * 
	 * User user2 = new User(); user2.setName(USER2_NAME);
	 * user2.setPassword(USER2_PASSWORD);
	 * user2.setRegistionDate(USER2_REGISTRATION_DATE); user2 =
	 * userRepository.save(user2);
	 * 
	 * UserGroup userGroup1 = new UserGroup();
	 * userGroup1.setName(GROUP_ALL_USERS); userGroup1.getUsers().add(user1);
	 * userGroup1.getUsers().add(user2);
	 * 
	 * UserGroup userGroup2 = new UserGroup(); userGroup2.setName(GROUP_ADMIN);
	 * userGroup2.getUsers().add(user1);// Only user1 is admin
	 * 
	 * userGroup1 = userGroupRepository.save(userGroup1); userGroup2 =
	 * userGroupRepository.save(userGroup2);
	 * 
	 * Assert.assertEquals(GROUP_ALL_USERS,
	 * userGroupRepository.findOne(userGroup1.getId()).getName());
	 * Assert.assertEquals(GROUP_ADMIN,
	 * userGroupRepository.findOne(userGroup2.getId()).getName());
	 * 
	 * List<UserGroup> groupList1 = new ArrayList<UserGroup>();
	 * groupList1.add(userGroup1); groupList1.add(userGroup2);
	 * 
	 * List<UserGroup> groupList2 = new ArrayList<UserGroup>();
	 * groupList2.add(userGroup2);
	 * 
	 * // System.out.println(userRepository.count(); // Assert.assertEquals(2,
	 * userRepository.findByUserGroupsIn(groupList1).size()); //TODO //
	 * Assert.assertEquals(1,
	 * userRepository.findByUserGroupsIn(groupList2).size());
	 * 
	 * 
	 * Assert.assertEquals(2,
	 * getGroupWithUsers(userGroup1.getId()).getUsers().size());
	 * Assert.assertEquals(1,
	 * getGroupWithUsers(userGroup2.getId()).getUsers().size());
	 * 
	 * 
	 * 
	 * movie.setCreator(user1);// user1 is already persited, so need call set //
	 * this object after persit.
	 * 
	 * Comment comment1 = new Comment(); comment1.setContent(COMMENT1_CONTENT);
	 * comment1.setCreationDate(COMMENT1_DATE); comment1.setCreator(user1);
	 * 
	 * Comment comment2 = new Comment(); comment2.setContent(COMMENT2_CONTENT);
	 * comment2.setCreationDate(COMMENT2_DATE); comment2.setCreator(user2);
	 * 
	 * movie.getComments().add(comment1); movie.getComments().add(comment2);
	 * 
	 * movie = movieRepository.save(movie); comment1.setMovie(movie);
	 * comment2.setMovie(movie);
	 * 
	 * commentRepository.save(comment1); commentRepository.save(comment2);
	 * 
	 * Assert.assertNotNull(movie.getId());
	 * 
	 * // movie = movieRepository.findOne(movie.getId()); // Integer version1 =
	 * movie.getVersion(); Assert.assertNotNull(movie);
	 * Assert.assertEquals(MOVIE_NAME, movie.getName());
	 * Assert.assertEquals(MOVIE_DESCRIPTION_1, movie.getDescription());
	 * Assert.assertEquals(MOVIE_CREATION_DATE, movie.getCreationDate());
	 * 
	 * Assert.assertEquals(USER1_NAME, movie.getCreator().getName());
	 * Assert.assertEquals(USER1_PASSWORD, movie.getCreator().getPassword());
	 * Assert.assertEquals(USER1_REGISTRATION_DATE, movie.getCreator()
	 * .getRegistionDate());
	 * 
	 * // Trigger lazy load of movie.comments // movie =
	 * getMovieWithComments(movie.getId()); // List<Comment> comments =
	 * movie.getComments();
	 * 
	 * List<Comment> comments = movie.getComments();
	 * 
	 * Assert.assertEquals(2, comments.size());
	 * Assert.assertEquals(COMMENT1_CONTENT, comments.get(0).getContent());
	 * Assert.assertEquals(COMMENT1_DATE, comments.get(0).getCreationDate());
	 * Assert.assertEquals(user1.getName(), comments.get(0).getCreator()
	 * .getName()); // Assert.assertEquals(movie.getId(), //
	 * comments.get(0).getMovie().getId());
	 * 
	 * Assert.assertEquals(COMMENT2_CONTENT, comments.get(1).getContent());
	 * Assert.assertEquals(COMMENT2_DATE, comments.get(1).getCreationDate());
	 * Assert.assertEquals(user2.getName(), comments.get(1).getCreator()
	 * .getName()); // Assert.assertEquals(movie.getId(), //
	 * comments.get(1).getMovie().getId());
	 * 
	 * movie.setDescription(MOVIE_DESCRIPTION_2); movie =
	 * movieRepository.save(movie); // Integer version2 = movie.getVersion();
	 * Assert.assertEquals(MOVIE_DESCRIPTION_2, movie.getDescription()); //
	 * Assert.assertTrue(version2 > version1);
	 * 
	 * // movieRepository.flush(); // Delete all. //
	 * movieRepository.deleteAll();
	 * 
	 * // Assert.assertEquals(0, movieRepository.count()); // // // Remove movie
	 * should not remove user. // Assert.assertEquals(2,
	 * userRepository.findAll().size()); // // // Remove movie should remove
	 * related comments // Assert.assertEquals(0, commentRepository.count());
	 * 
	 * }
	 */

	Movie movie = null;
	//
	// @Transactional
	// private Movie getMovieWithComments(long movieID) {
	// TransactionTemplate transactionTemplate = new TransactionTemplate(
	// transactionManager);
	// transactionTemplate.execute(new TransactionCallbackWithoutResult() {
	// @Override
	// protected void doInTransactionWithoutResult(TransactionStatus status) {
	// movie = movieRepository.findOne(movieID);
	// movie.getComments().size();
	// movie.getComments().sort(new Comparator<Comment>() {
	// @Override
	// public int compare(Comment comment1, Comment comment2) {
	// return comment2.getCreationDate().compareTo(
	// comment1.getCreationDate());
	// }
	// });
	// }
	// });
	// return movie;
	// }

	UserGroup userGroup = null;

	@Transactional
	private UserGroup getGroupWithUsers(long userGroupId) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(
				transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				userGroup = userGroupRepository.findOne(userGroupId);
				userGroup.getUsers().size();
				userGroup.getUsers().sort(new Comparator<User>() {
					@Override
					public int compare(User user1, User user2) {
						return user1.getName().compareTo(user2.getName());
					}
				});
			}
		});
		return userGroup;
	}

}
