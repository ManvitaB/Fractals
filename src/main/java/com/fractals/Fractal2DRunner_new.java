package com.fractals;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.fractals.DB.Fractal2DEntity;

/**
 * Fractal2DRunner_new --- Generates and outputs a Fractal2D as an image to the disk 
 * 					       asynchronously via CompletableFuture<String>.
 * TODO delete Fractal2DRunner and rename this to Fractal2DRunner.
 * @author Scott Wolfskill
 * @created     02/18/2019
 * @last_edit   03/02/2019
 */
public class Fractal2DRunner_new 
{
	private final static String staticDir = "static/";
	
	private Fractal2DRunner_new() {}
	
	/**
	 * Queues a Fractal2D for asynchronous generation/output to file as PNG image 
	 * and saves its Fractal2DEntity to its respective repository, unless a T with the same
	 * parameters as toGenerate is found in the database, indicating it is already generating or has been generated. 
	 * @param toGenerate Derived class of Fractal2DEntity to generate its fractal2D.
	 * @param allowDuplicates If true, will always add toGenerate to its respective DB. If false, won't add if a match is found.
	 * @return identical T found in the database (if allowDuplicates is true), or null if toGenerate was added to the DB.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Fractal2DEntity<S>, S extends Fractal2D> T generate(T toGenerate, boolean allowDuplicates)
	{
		//If duplicates aren't allowed, check the DB first for a match to toGenerate.
		if(!allowDuplicates) {
			Optional<Fractal2DEntity<S>> found = toGenerate.defaultRepository().findByFractal2D(toGenerate.getFractal2D());
			if(found.isPresent())
			{
				//Case 1: Identical T already exists in DB; generation task complete or in-progress. 
				return (T) found.get();
			}
		}
		//Case 2: New async generation task: not in DB, not generated yet.
		toGenerate.defaultRepository().save(toGenerate);
		generateAndOutputToFile_async(toGenerate);
		return null; //was not in database beforehand
	}
	
	private static <T extends Fractal2DEntity<S>, S extends Fractal2D> void generateAndOutputToFile_async(T toGenerate)
	{
		Date start = new Date();
		String relativePath = staticDir + Helper.getDirectoriesFromPath(toGenerate.getImageSrc());
		String filename = Helper.getFilenameFromPath(toGenerate.getImageSrc());
		CompletableFuture.supplyAsync(() -> {
			Fractal2D fractal2D = toGenerate.getFractal2D();
			final String fractalType = fractal2D.getClass().getSimpleName();
			System.out.println(fractalType + " runner started.");
			//1. Generate fractal2D
			String loadingMessage;
			fractal2D.generate();
			
			//2. Attempt to output generated fractal2D to file
			try 
			{
				String fullPath = fractal2D.outputToFile(relativePath, filename, "png");
				loadingMessage = "Generated at " + fullPath;
			} 
			catch (Exception e)
			{
				loadingMessage = "Could not output " + fractalType + " to file: '" 
						+ e.getClass().toString() + ": "+ e.getMessage() + "'";
				System.out.println(loadingMessage);
			}
			
			//3. Update loadingMessage and generationComplete in DB and return
			Date end = new Date();
			long elapsed = end.getTime() - start.getTime();
			toGenerate.setLoadingMessage(loadingMessage);
			toGenerate.setGenerationComplete(true);
			toGenerate.setGenerationTime(elapsed);
			toGenerate.defaultRepository().save(toGenerate);
			
			if(fractal2D.cancelled.get()) {
				System.out.println("Cancelled " + fractalType + " runner finished.");
			} else {
				System.out.println(fractalType + " runner finished in " + elapsed + " ms : '" + loadingMessage + "'");
			}
			return loadingMessage;
		});
	}
	
	//TODO implement cancelling an in-progress generation task.
}
