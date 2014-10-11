package com.jimmystudio.movietime_service.repository;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

	// Movie Constants
	private static final String MOVIE_NAME = "Titanic";
	private static final Date MOVIE_CREATION_DATE = new DateTime().plus(
			counter++).toDate();
	private static final String MOVIE_DESCRIPTION_1 = "Good movie!";
	private static final String MOVIE_DESCRIPTION_2 = "Very goold movie";

	// User1 Constants
	private static final String USER1_NAME = "Jack";
	private static final String USER1_PASSWORD = "password1";
	private static final Date USER1_REGISTRATION_DATE = new DateTime().plus(
			counter++).toDate();

	// User2 Constants
	private static final String USER2_NAME = "David";
	private static final String USER2_PASSWORD = "password2";
	private static final Date USER2_REGISTRATION_DATE = new DateTime().plus(
			counter++).toDate();

	// Comment1 Constants
	private static final String COMMENT1_CONTENT = "Comment1 Content";
	private static final Date COMMENT1_DATE = new DateTime().plus(counter++)
			.toDate();

	// Comment2 Constants
	private static final String COMMENT2_CONTENT = "Comment2 Content";
	private static final Date COMMENT2_DATE = new DateTime().plus(counter++)
			.toDate();

	// Group1 Constants
	private static final String GROUP1_ALL_USERS = "All User Group";

	// Group2 Constants
	private static final String GROUP2_ADMIN = "Admin Group";

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

		boolean isDebug = java.lang.management.ManagementFactory
				.getRuntimeMXBean().getInputArguments().toString()
				.indexOf("-agentlib:jdwp") > 0;

		if (isDebug && isPortAvailable(Constants.DEFAULT_HTTP_PORT)) {
			server.start();
			Server.openBrowser("http://localhost:"
					+ Constants.DEFAULT_HTTP_PORT);
		}

	}

	@After
	public void tearDown() {
		if (server.isRunning(true)) {
			server.stop();
		}
	}

	@Test
	public void testSingleMovieRepositoryAutoCommit() {

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

		User user1 = new User();
		user1.setName(USER1_NAME);
		user1.setPassword(USER1_PASSWORD);
		user1.setRegistionDate(USER1_REGISTRATION_DATE);
		movie.setCreator(user1);
		
		
		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is mandatory for Movie and it's not yet created before saving, and Exception not throw here.");
		} catch (InvalidDataAccessApiUsageException e) {
			// Expected here.
		}

		//Setup users and groups		
		user1 = userRepository.save(user1);

		User user2 = new User();
		user2.setName(USER2_NAME);
		user2.setPassword(USER2_PASSWORD);
		user2.setRegistionDate(USER2_REGISTRATION_DATE);
		user2 = userRepository.save(user2);

		UserGroup userGroup1 = new UserGroup();
		userGroup1.setName(GROUP1_ALL_USERS);
		userGroup1.getUsers().add(user1);
		userGroup1.getUsers().add(user2);

		UserGroup userGroup2 = new UserGroup();
		userGroup2.setName(GROUP2_ADMIN);
		userGroup2.getUsers().add(user1);// Only user1 is admin

		userGroup1 = userGroupRepository.save(userGroup1);
		userGroup2 = userGroupRepository.save(userGroup2);


		Assert.assertEquals(GROUP1_ALL_USERS,
				userGroupRepository.findOne(userGroup1.getId()).getName());
		Assert.assertEquals(GROUP2_ADMIN,
				userGroupRepository.findOne(userGroup2.getId()).getName());

		//Through userRepository to get users under groups
		List<UserGroup> groupList1 = new ArrayList<UserGroup>();
		groupList1.add(userGroup1);
		List<UserGroup> groupList2 = new ArrayList<UserGroup>();
		groupList2.add(userGroup2);
		Assert.assertEquals(2, userRepository.findByUserGroupsIn(groupList1)
				.size());
		Assert.assertEquals(1, userRepository.findByUserGroupsIn(groupList2)
				.size());

		//Through userGroupRepository to get groups contain users
		List<User> userList1 = new ArrayList<User>();
		userList1.add(user1);
		List<User> userList2 = new ArrayList<User>();
		userList2.add(user2);
		Assert.assertEquals(2, userGroupRepository.findByUsersIn(userList1)
				.size());
		Assert.assertEquals(1, userGroupRepository.findByUsersIn(userList2)
				.size());
		
		//Use lazy load approach to load users under group
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

		movie.setCreator(user1);// user1 is already persited, so need call set
		// this object after persit.

		movie = movieRepository.save(movie);//Save a movie without any comment.

		comment1.setMovie(movie);
		comment2.setMovie(movie);

		commentRepository.save(comment1);
		commentRepository.save(comment2);

		Assert.assertNotNull(movie.getId());

		movie = movieRepository.findOne(movie.getId());
		
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

		//Update movie and check if version is updated.
		Integer version1 = movie.getVersion();
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

	Movie movie = null;

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
	
	
	
	private boolean isPortAvailable(int port) {
	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}

}
