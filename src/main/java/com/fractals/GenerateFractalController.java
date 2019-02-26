package com.fractals;

import java.awt.List;
import java.util.Date;
import java.util.LinkedList;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * GenerateFractalController --- 
 * 			Holds the logic to map user-inputted URI to fractal generation via Spring
 * 		    and then display the fractal HTML page.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   02/25/2019
 */
@Controller
public class GenerateFractalController 
{
	private Fractal2DRunner fractal2DRunner = null;
	private Model model = null;
	
	/**
	 * Queue a FractalTree for async generation, and return fractal.html
	 * with params fragment set to fragments/fractal-tree-params.html
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
		String relativePath = "static/images/";
		String filename = "fractal-tree.png";
		double angle_rad = Math.toRadians(angle);
		
		FractalTree fractalTree = new FractalTree(width, height, iterations, angle_rad,
												  factor, padding_w, padding_h);
		//Generate on separate thread using Fractal2DRunner
		Fractal2DRunner.ModelParamSetter modelParamSetter = initFractal2DRunner(model);
		
		setFractal2DModelParams("Fractal Tree", "/fractal-tree", "images/" + filename, 
				"fragments/fractal-tree-params.html", width, height, iterations, model);
		
		fractal2DRunner.generateAndOutputToFile(modelParamSetter,
				fractalTree, relativePath, filename, true);
		
		//Set FractalTree-specific attributes
		model.addAttribute("angle", angle);
		model.addAttribute("factor", factor);
		
		return "fractal";
	}
	
	/**
	 * Queue a FractalCircle for async generation, and return fractal.html
	 * with params fragment set to fragments/fractal-circle-params.html
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
			@RequestParam(name="i", required=false, defaultValue="4") int iterations,
			@RequestParam(name="satellites", required=false, defaultValue="4") int satellites,
			@RequestParam(name="factor", required=false, defaultValue="0.5") double factor,
			@RequestParam(name="padding_w", required=false, defaultValue="40") int padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") int padding_h,
			Model model)
	{
		String relativePath = "static/images/";
		String filename = "fractal-circle.png";
		
		FractalCircle fractalCircle = new FractalCircle(width, height, iterations, satellites,
												  	    factor, padding_w, padding_h);

		//Generate on separate thread using Fractal2DRunner
		Fractal2DRunner.ModelParamSetter modelParamSetter = initFractal2DRunner(model);
		
		setFractal2DModelParams("Fractal Circles", "/fractal-circle", "images/" + filename, 
				"fragments/fractal-circle-params.html", width, height, iterations, model);
		
		fractal2DRunner.generateAndOutputToFile(modelParamSetter,
				fractalCircle, relativePath, filename, true);
		
		//Set FractalCircle-specific attributes
		model.addAttribute("satellites", satellites);
		model.addAttribute("factor", factor);
		
		return "fractal";
	}
	
	/**
	 * Return the String value of the page model's loadingMessage attribute.
	 * @return ResponseEntity containing a single String
	 */
	@GetMapping("/get-loading-message")
	public @ResponseBody ResponseEntity<?> getLoadingMessage()
	{
		if(model != null) {
			String loadingMessage = (String) model.asMap().get("loadingMessage");
			return ResponseEntity.ok(loadingMessage);
		} else {
			String errMessage = "No loading message found; no fractal generation has been attempted.";
			return ResponseEntity.badRequest().body(errMessage);
		}
	}
	
	private Fractal2DRunner.ModelParamSetter initFractal2DRunner(Model model)
	{
		if(fractal2DRunner == null) {
			fractal2DRunner = new Fractal2DRunner();
		}
		
		Fractal2DRunner.ModelParamSetter modelParamSetter = (String loadingMessage) ->
		{
			setFractal2DModelParam_loadingMessage(loadingMessage, model);
		};
		return modelParamSetter;
	}
	
	private void setFractal2DModelParams(String title, String action, String imagePath, String params_page, 
			   							 int width, int height, int iterations, Model model)
	{
		model.addAttribute("title", title);
		model.addAttribute("action", action);
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("params_page", params_page);
		model.addAttribute("params_fragment", "params");
		model.addAttribute("width", width);
		model.addAttribute("height", height);
		model.addAttribute("iterations", iterations);
		model.addAttribute("loadingMessage", "Generating...");
		this.model = model;
	}
	
	private void setFractal2DModelParam_loadingMessage(String loadingMessage, Model model)
	{
		// This updates server-side model only. The client must get the updated attribute with 
		// URI /get-loading-message
		model.addAttribute("loadingMessage", loadingMessage);
	}
}
