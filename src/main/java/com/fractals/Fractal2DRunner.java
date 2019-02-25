package com.fractals;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Fractal2DRunner --- Generates and outputs a Fractal2D as an image to the disk 
 * 					   asynchronously via CompletableFuture<T>.
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
		public void setModelParams(String loadingMessage);
	}
	
	
	public Fractal2DRunner() {
		this.fractal2D = null;
		this.runner = null;
	}
	
	public void generateAndOutputToFile(ModelParamSetter modelParamMethod, Fractal2D fractal2D,
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
			modelParamMethod.setModelParams(loadingMessage);
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
