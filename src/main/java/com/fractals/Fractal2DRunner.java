package com.fractals;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Fractal2DRunner --- Generates and outputs a Fractal2D as an image to the disk 
 * 					   asynchronously via CompletableFuture<String>.
 * @author Scott Wolfskill
 * @created     02/18/2019
 * @last_edit   02/25/2019
 */
@Service
public class Fractal2DRunner 
{
	private Fractal2D fractal2D;
	private CompletableFuture<String> runner;
	
	public interface ModelParamSetter
	{
		/**
		 * Method that typically updates a Thymeleaf page model attribute with loadingMessage.
		 * @param loadingMessage Status message on fractal generation.
		 */
		public void setModelParams(String loadingMessage);
	}
	
	public Fractal2DRunner() {
		this.fractal2D = null;
		this.runner = null;
	}
	
	/**
	 * Queues a Fractal2D for asynchronous generation/output to file as PNG image
	 * at relativePath/filename, using CompletableFuture. Optionally cancels in-progress generation task first.
	 * @param modelParamSetter Interface (typically lambda exp.) implementing ModelParamSetter.
	 * @param relativePath Relative directory that filename is within.
	 * @param filename Filename (with extension) of the image to output the generated fractal to.
	 * @param cancelIfRunning If true, will cancel an in-progress generation before starting the new generation. 
	 */
	public void generateAndOutputToFile(ModelParamSetter modelParamSetter, Fractal2D fractal2D,
			String relativePath, String filename, boolean cancelIfRunning) 
	{
		Date start = new Date();
		//1. Cancel any currently running task if cancelIfRunning is true
		if(runner != null && !runner.isDone() && cancelIfRunning)
		{
			runner.cancel(true);
			this.fractal2D.cancelled.set(true);
			System.out.println("Cancelling already-running Fractal2D.");
		}
		this.fractal2D = fractal2D;
		runner = CompletableFuture.supplyAsync(() -> {
			System.out.println("Fractal2D runner started.");
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
				loadingMessage = "Could not output fractal2D to file: '" 
						+ e.getClass().toString() + ": "+ e.getMessage() + "'";
				System.out.println(loadingMessage);
			}
			
			//3. Update Thymeleaf page model's params and return
			modelParamSetter.setModelParams(loadingMessage);
			if(fractal2D.cancelled.get()) {
				System.out.println("Cancelled Fractal2D runner finished.");
			} else {
				Date end = new Date();
				long elapsed = end.getTime() - start.getTime();
				System.out.println("Fractal2D runner finished in " + elapsed + " ms : '" + loadingMessage + "'");
			}
			return loadingMessage;
		});
	}
	
}
