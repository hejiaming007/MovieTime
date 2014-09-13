package com.jimmystudio.movietime_service.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jimmystudio.movietime_service.entity.Comment;
import com.jimmystudio.movietime_service.entity.Movie;
import com.jimmystudio.movietime_service.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MovieRepositoryTest {

	private static final Date MOVIE_CREATION_DATE = new Date();
	private static final Date CREATOR_REGISTRATION_DATE = new Date();
	private static final String MOVIE_NAME = "Titanic";
	private static final String MOVIE_DESCRIPTION_1 = "Good movie!";
	private static final String MOVIE_DESCRIPTION_2 = "Very goold movie";
	private static final String CREATOR_NAME = "Jimmy";
	private static final String CREATOR_PASSWORD = "password";

	private static final String COMMENT1_CONTENT = "Comment1 Content";
	private static final String COMMENT2_CONTENT = "Comment2 Content";
	private static final Date COMMENT1_DATE = new Date();
	private static final Date COMMENT2_DATE = new Date();

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	public void testSingleMovieRepository() {

		Assert.assertNotNull(movieRepository);
		Assert.assertNotNull(userRepository);

		Movie movie = new Movie();
		movie.setName(MOVIE_NAME);
		movie.setCreationDate(MOVIE_CREATION_DATE);
		movie.setDescription(MOVIE_DESCRIPTION_1);

		Comment comment1 = new Comment();
		comment1.setContent(COMMENT1_CONTENT);
		comment1.setCreationDate(COMMENT1_DATE);
		comment1.setMovie(movie);
		movie.getComments().add(comment1);

		Comment comment2 = new Comment();
		;
		comment2.setContent(COMMENT2_CONTENT);
		comment2.setCreationDate(COMMENT2_DATE);
		comment2.setMovie(movie);
		movie.getComments().add(comment2);

		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is null but not throw Exception here.");
		} catch (JpaSystemException e) {
			// Expected here.
		}

		User creator = new User();
		creator.setName(CREATOR_NAME);
		creator.setPassword(CREATOR_PASSWORD);
		creator.setRegistionDate(CREATOR_REGISTRATION_DATE);

		try {
			movie = movieRepository.save(movie);
			Assert.fail("Creator is not yet created, but not throw Exception here.");
		} catch (Exception e) {
			// Expected here.
		}

		creator = userRepository.save(creator);
		movie.setCreator(creator);
		movie = movieRepository.save(movie);

		Assert.assertNotNull(movie.getId());

		movie = movieRepository.findOne(movie.getId());
		Integer version1 = movie.getVersion();
		Assert.assertNotNull(movie);
		Assert.assertEquals(MOVIE_NAME, movie.getName());
		Assert.assertEquals(MOVIE_DESCRIPTION_1, movie.getDescription());
		Assert.assertEquals(MOVIE_CREATION_DATE, movie.getCreationDate());

		Assert.assertEquals(CREATOR_NAME, movie.getCreator().getName());
		Assert.assertEquals(CREATOR_PASSWORD, movie.getCreator().getPassword());
		Assert.assertEquals(CREATOR_REGISTRATION_DATE, movie.getCreator()
				.getRegistionDate());

		// Load comment by movie id on demand.
		Assert.assertEquals(1, movieRepository.findCommentsById(movie.getId())
				.size());

		movie.setDescription(MOVIE_DESCRIPTION_2);
		movie = movieRepository.save(movie);
		Integer version2 = movie.getVersion();
		Assert.assertEquals(MOVIE_DESCRIPTION_2, movie.getDescription());
		Assert.assertTrue(version2 > version1);

		// Delete all.
		movieRepository.deleteAll();

		Assert.assertEquals(0, movieRepository.count());

		// Remove movie should not remove user.
		Assert.assertEquals(1, userRepository.findAll().size());

	}

}
