package com.fractals;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * Generate a FractalTree and go to fractal.html
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param angle Angle in degrees between child nodes in the fractal.
	 * @param factor Scaling factor for each child node in the fractal.
	 * @param padding_w Horizontal padding in the image to generate.
	 * @param padding_h Vertical padding in the image to generate.
	 * @param model Thymeleaf page model
	 * @return
	 */
	@GetMapping("/fractal-tree")
	public String generateFractal(
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
		fractalTree.generate();
		try 
		{
			String fullPath = fractalTree.outputToFile(relativePath, filename, "png");
			loadingMessage = "Generated at " + fullPath;
		} 
		catch (Exception e)
		{
			loadingMessage = "Could not output fractal tree to file: '" 
					+ e.getClass().toString() + ": "+ e.getMessage() + "'";
			System.out.println(loadingMessage);
		}
		model.addAttribute("width", width);
		model.addAttribute("height", height);
		model.addAttribute("iterations", iterations);
		model.addAttribute("angle", angle);
		model.addAttribute("factor", factor);
		model.addAttribute("loadingMessage", loadingMessage);
		return "fractal";
	}
}
