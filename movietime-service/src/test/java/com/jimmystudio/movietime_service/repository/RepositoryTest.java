package com.jimmystudio.movietime_service.repository;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jimmystudio.movietime_service.entity.Comment;
import com.jimmystudio.movietime_service.entity.Group;
import com.jimmystudio.movietime_service.entity.Movie;
import com.jimmystudio.movietime_service.entity.User;

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

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	JpaRepository<Movie, Long> movieRepository;

	@Autowired
	JpaRepository<User, Long> userRepository;

	@Autowired
	JpaRepository<Group, Long> groupRepository;

	@Test
	public void testSingleMovieRepository() {

		Assert.assertNotNull(movieRepository);
		Assert.assertNotNull(userRepository);

		Movie movie = new Movie();
		movie.setName(MOVIE_NAME);
		movie.setCreationDate(MOVIE_CREATION_DATE);
		movie.setDescription(MOVIE_DESCRIPTION_1);


//		comment1.setMovie(movie);
//		movie.getComments().add(comment1);


//		comment2.setMovie(movie);
//		movie.getComments().add(comment2);

		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is null but not throw Exception here.");
		} catch (JpaSystemException e) {
			// Expected here.
		}

		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is not yet created, but not throw Exception here.");
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

		movie.setCreator(user1);// user1 is already persited, so need call set
								// this object after persit.
		
		Comment comment1 = new Comment();
		comment1.setContent(COMMENT1_CONTENT);
		comment1.setCreationDate(COMMENT1_DATE);
		comment1.setCreator(user1);
		
		Comment comment2 = new Comment();
		comment2.setContent(COMMENT2_CONTENT);
		comment2.setCreationDate(COMMENT2_DATE);
		comment2.setCreator(user2);
//		movie.getComments().get(0).setCreator(user1);
//		movie.getComments().get(1).setCreator(user2);
		
		movie.getComments().add(comment1);
		movie.getComments().add(comment2);
		
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

		// Load comment by movie id on demand.
		List<Comment> comments = commentRepository
				.findByMovieOrderByCreationDateDesc(movie);
		Assert.assertEquals(2, comments.size());
		Assert.assertEquals(COMMENT2_CONTENT, comments.get(0).getContent());
		Assert.assertEquals(COMMENT2_DATE, comments.get(0).getCreationDate());
		Assert.assertEquals(user2.getName(), comments.get(0).getCreator()
				.getName());
//		Assert.assertEquals(movie.getId(), comments.get(0).getMovie().getId());

		Assert.assertEquals(COMMENT1_CONTENT, comments.get(1).getContent());
		Assert.assertEquals(COMMENT1_DATE, comments.get(1).getCreationDate());
		Assert.assertEquals(user1.getName(), comments.get(1).getCreator()
				.getName());
//		Assert.assertEquals(movie.getId(), comments.get(1).getMovie().getId());

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

	}

}
