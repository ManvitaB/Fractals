package com.fractals;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * GenerateFractalController --- 
 * 			Holds the logic to map user-inputted URI to fractal generation via Spring
 * 		    and then display the fractal HTML page.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   02/12/2019
 */
@Controller
public class GenerateFractalController 
{	
	/**
	 * Generate a FractalTree, go to fractal.html,
	 * and load params fragment at fragments/fractal-tree-params.html
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param angle Angle in degrees between child nodes in the fractal.
	 * @param factor Scaling factor for each child node in the fractal.
	 * @param padding_w Horizontal padding in the image to generate.
	 * @param padding_h Vertical padding in the image to generate.
	 * @param model Thymeleaf page model
	 * @return fractal.html
	 */
	@GetMapping("/fractal-tree")
	public String generateFractalTree(
			@RequestParam(name="w", required=false, defaultValue="500") int width,
			@RequestParam(name="h", required=false, defaultValue="500") int height,
			@RequestParam(name="i", required=false, defaultValue="10") int iterations,
			@RequestParam(name="angle", required=false, defaultValue="60") double angle,
			@RequestParam(name="factor", required=false, defaultValue="0.77") double factor,
			@RequestParam(name="padding_w", required=false, defaultValue="40") int padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") int padding_h,
			Model model)
	{
		String loadingMessage = "";
		String relativePath = "static/images/";
		String filename = "fractal-tree.png";
		double angle_rad = Math.toRadians(angle);
		//For now, generate on same thread
		FractalTree fractalTree = new FractalTree(width, height, iterations, angle_rad,
												  factor, padding_w, padding_h);
		loadingMessage = generateFractal2D(fractalTree, relativePath, filename);
		
		setFractal2DModelParams("Fractal Tree", "images/" + filename, 
				"fragments/fractal-tree-params.html", loadingMessage, width, height, iterations, model);
		
		//Set FractalTree-specific attributes
		model.addAttribute("angle", angle);
		model.addAttribute("factor", factor);
		
		return "fractal";
	}
	
	/**
	 * Generate a FractalCircle, go to fractal.html
	 * and load params fragment at fragments/fractal-circle-params.html
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param satellites Number of child node 'satellites' to generate per iteration.
	 * @param factor Scaling factor for each child node in the fractal.
	 * @param padding_w Horizontal padding in the image to generate.
	 * @param padding_h Vertical padding in the image to generate.
	 * @param model Thymeleaf page model
	 * @return fractal.html
	 */
	@GetMapping("/fractal-circle")
	public String generateFractalCircle(
			@RequestParam(name="w", required=false, defaultValue="700") int width,
			@RequestParam(name="h", required=false, defaultValue="500") int height,
			@RequestParam(name="i", required=false, defaultValue="10") int iterations,
			@RequestParam(name="satellites", required=false, defaultValue="4") int satellites,
			@RequestParam(name="factor", required=false, defaultValue="0.5") double factor,
			@RequestParam(name="padding_w", required=false, defaultValue="40") int padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") int padding_h,
			Model model)
	{
		String loadingMessage = "";
		String relativePath = "static/images/";
		String filename = "fractal-circle.png";
		//For now, generate on same thread
		FractalCircle fractalCircle = new FractalCircle(width, height, iterations, satellites,
												  	    factor, padding_w, padding_h);
		loadingMessage = generateFractal2D(fractalCircle, relativePath, filename);
		
		setFractal2DModelParams("Fractal Circles", "images/" + filename, 
				"fragments/fractal-circle-params.html", loadingMessage, width, height, iterations, model);
		
		//Set FractalCircle-specific attributes
		model.addAttribute("satellites", satellites);
		model.addAttribute("factor", factor);
		
		return "fractal";
	}
	
	private String generateFractal2D(Fractal2D fractal2D, String relativePath, String filename)
	{
		String loadingMessage;
		fractal2D.generate();
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
		return loadingMessage;
	}
	
	private void setFractal2DModelParams(String title, String imagePath, String params_page, 
				   String loadingMessage, int width, int height, int iterations, Model model)
	{
		model.addAttribute("title", title);
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("params_page", params_page);
		model.addAttribute("params_fragment", "params");
		model.addAttribute("loadingMessage", loadingMessage);
		model.addAttribute("width", width);
		model.addAttribute("height", height);
		model.addAttribute("iterations", iterations);
	}
}
