package com.fractals;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fractals.DB.DB;
import com.fractals.DB.Fractal2DEntity;
import com.fractals.DB.Fractal2DEntityRepository;
import com.fractals.DB.FractalCircleEntity;
import com.fractals.DB.FractalTreeEntity;

/**
 * GenerateFractalController --- 
 * 			Holds the logic to map user-inputted URI to fractal generation via Spring
 * 		    and then display the fractal HTML page.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   02/28/2019
 */
@Controller
public class GenerateFractalController 
{	
	private static Fractal2DRunner fractal2DRunner = null;
	private static Model model = null;
	
	private final String staticDir = "static/";
	private final String defaultFractalImageDirectory = "images/";
	private final String defaultFractalTreeFilename = "fractal-tree.png";
	private final String defaultFractalTreePath = defaultFractalImageDirectory + defaultFractalTreeFilename;
	private final String defaultFractalCircleFilename = "fractal-circle.png";
	private final String defaultFractalCirclePath = defaultFractalImageDirectory + defaultFractalCircleFilename;
	
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
	 * @param fractalImagePath Relative path (inside "static/" directory) to generate fractal image at.
	 * @param model Thymeleaf page model
	 * @return fractal.html
	 */
	@GetMapping("/fractal-tree")
	public String generateFractalTree(
			@RequestParam(name="w", required=false, defaultValue="500") String _width,
			@RequestParam(name="h", required=false, defaultValue="500") String _height,
			@RequestParam(name="i", required=false, defaultValue="10") String _iterations,
			@RequestParam(name="angle", required=false, defaultValue="60") String _angle,
			@RequestParam(name="factor", required=false, defaultValue="0.77") String _factor,
			@RequestParam(name="padding_w", required=false, defaultValue="40") String _padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") String _padding_h,
			@RequestParam(name="src", required=false, defaultValue=defaultFractalTreePath) String fractalImagePath,
			Model model)
	{
		//Attempt to parse params to numerical types
		Helper.Wrapper<String> parseFailedMessage = new Helper.Wrapper<String>("");
		try 
		{
			int width = Helper.parseNonNegativeIntParam("Width", _width, parseFailedMessage);
			int height = Helper.parseNonNegativeIntParam("Height", _height, parseFailedMessage);
			int iterations = Helper.parseNonNegativeIntParam("Iterations", _iterations, parseFailedMessage);
			double angle = Helper.parseDoubleParam("Angle", _angle, parseFailedMessage);
			double factor = Helper.parseDoubleParam("Scaling Factor", _factor, parseFailedMessage);
			int padding_w = Helper.parseNonNegativeIntParam("padding_w", _padding_w, parseFailedMessage);
			int padding_h = Helper.parseNonNegativeIntParam("padding_h", _padding_h, parseFailedMessage);
			
			//Parse success: perform actual generation
			return _generateFractalTree(width, height, iterations, angle, factor, padding_w, 
										padding_h, fractalImagePath, model);
		} 
		catch (NumberFormatException e) 
		{
			//Parsing failed
			System.out.println("generateFractalTree: " + parseFailedMessage.value);
			model.addAllAttributes(GenerateFractalController.model.asMap());
			model.addAttribute("parseFailedMessage", parseFailedMessage.value);
			return "fractal";
		}
	}
	
	private String _generateFractalTree(int width, int height, int iterations, double angle, double factor, 
										int padding_w, int padding_h, String fractalImagePath, Model model)
	{
		double angle_rad = Math.toRadians(angle);
		
		FractalTree fractalTree = new FractalTree(width, height, iterations, angle_rad,
												  factor, padding_w, padding_h);
		//TODO temp remove: add FractalCircleEntity to the DB
		FractalTreeEntity fractalTreeEntity = new FractalTreeEntity(fractalTree, fractalImagePath, "Generating...", 
				new java.sql.Date(new Date().getTime()), new Boolean(false));
		//fractalTreeEntries.save(fractalTreeEntity);
		DB.getFractalTreeEntities().save(fractalTreeEntity);
		//end temp
		// Set FractalTree-specific attributes
		model.addAttribute("angle", angle);
		model.addAttribute("factor", factor);
		
		// Generate on separate thread using Fractal2DRunner, if not duplicate request
		generateFractal2D(fractalTree, fractalImagePath, "Fractal Tree", "/fractal-tree", 
				  "fragments/fractal-tree-params.html", model);
		
		return "fractal";
	}
	
	//TODO temp for debugging
	@GetMapping("/db-all-trees")
	public @ResponseBody Iterable<FractalTreeEntity> listDB_fractalTrees()
	{
		//return fractalTreeEntries.findAll();
		return DB.getFractalTreeEntities().findAll();
	}
	
	//TODO temp for debugging
	@GetMapping("/db-all-circles")
	public @ResponseBody Iterable<FractalCircleEntity> listDB_fractalCircles()
	{
		//return fractalCircleEntries.findAll();
		return DB.getFractalCircleEntities().findAll();
		/*ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
		DB db = context.getBean("db", DB.class);
		System.out.println(db.fractalTreeEntities);
		System.out.println(db.fractalCircleEntities);
		return db.fractalCircleEntities.findAll();*/
		//throws long exception about can't find FractalTreeEntityRepository bean
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
	 * @param fractalImagePath Relative path (inside "static/" directory) to generate fractal image at.
	 * @param model Thymeleaf page model
	 * @return fractal.html
	 */
	@GetMapping("/fractal-circle")
	public String generateFractalCircle(
			@RequestParam(name="w", required=false, defaultValue="700") String _width,
			@RequestParam(name="h", required=false, defaultValue="500") String _height,
			@RequestParam(name="i", required=false, defaultValue="4") String _iterations,
			@RequestParam(name="satellites", required=false, defaultValue="4") String _satellites,
			@RequestParam(name="factor", required=false, defaultValue="0.5") String _factor,
			@RequestParam(name="padding_w", required=false, defaultValue="40") String _padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") String _padding_h,
			@RequestParam(name="src", required=false, defaultValue=defaultFractalCirclePath) String fractalImagePath,
			Model model)
	{
		// Attempt to parse params to numerical types
		Helper.Wrapper<String> parseFailedMessage = new Helper.Wrapper<String>("");
		try 
		{
			int width = Helper.parseNonNegativeIntParam("Width", _width, parseFailedMessage);
			int height = Helper.parseNonNegativeIntParam("Height", _height, parseFailedMessage);
			int iterations = Helper.parseNonNegativeIntParam("Iterations", _iterations, parseFailedMessage);
			int satellites = Helper.parseNonNegativeIntParam("Satellites", _satellites, parseFailedMessage);
			double factor = Helper.parseDoubleParam("Scaling Sactor", _factor, parseFailedMessage);
			int padding_w = Helper.parseNonNegativeIntParam("padding_w", _padding_w, parseFailedMessage);
			int padding_h = Helper.parseNonNegativeIntParam("padding_h", _padding_h, parseFailedMessage);
			
			//Parse success: perform actual generation
			return _generateFractalCircle(width, height, iterations, satellites, factor, padding_w, 
										  padding_h, fractalImagePath, model);
		} 
		catch (NumberFormatException e) 
		{
			// Parsing failed
			System.out.println("generateFractalCircle: " + parseFailedMessage.value);
			model.addAllAttributes(GenerateFractalController.model.asMap());
			model.addAttribute("parseFailedMessage", parseFailedMessage.value);
			return "fractal";
		}
	}
	
	private String _generateFractalCircle(int width, int height, int iterations, int satellites,
			double factor, int padding_w, int padding_h, String fractalImagePath, Model model)
	{
		FractalCircle fractalCircle = new FractalCircle(width, height, iterations, satellites,
												  	    factor, padding_w, padding_h);
		// TODO temp remove: add FractalTreeEntity to the DB
		FractalCircleEntity fractalCircleEntity = new FractalCircleEntity(fractalCircle, fractalImagePath, "Generating...",
				new java.sql.Date(new Date().getTime()), new Boolean(false));
		//fractalCircleEntries.save(fractalCircleEntity);
		DB.getFractalCircleEntities().save(fractalCircleEntity);
		// end temp
		// Set FractalCircle-specific attributes
		model.addAttribute("satellites", satellites);
		model.addAttribute("factor", factor);
					
		// Generate on separate thread using Fractal2DRunner, if not duplicate request
		generateFractal2D(fractalCircle, fractalImagePath, "Fractal Circles", "/fractal-circle", 
						  "fragments/fractal-circle-params.html", model);
		
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
	
	/**
	 * Queue a Fractal2D for asynchronous generation using fractal2DRunner (unless duplicate request)
	 * and set a Thymeleaf page model attributes.
	 * @param toGenerate Fractal2D to generate & output to file asynchronously.
	 * @param fractalImagePath Relative path of the output image to generate. (e.g. "images/fractal2d.png")
	 * @param title Title of the Thymeleaf page to set.
	 * @param action Action URI of the Fractal2D to generate.
	 * @param params_page Thymeleaf fragment to load which holds form parameters for the fractal2D.
	 * @param model Thymeleaf page model to set the attributes of.
	 * @return true if queued for generation, false if duplicate request.
	 */
	private boolean generateFractal2D(Fractal2D toGenerate, String fractalImagePath, String title, 
									  String action, String params_page, Model model)
	{
		String relativePath = staticDir + Helper.getDirectoriesFromPath(fractalImagePath);
		String filename = Helper.getFilenameFromPath(fractalImagePath);
		String relativePath_full = relativePath + filename;
		Fractal2DRunner.ModelParamSetter modelParamSetter = initFractal2DRunner(model);
		
		//Generate on separate thread using Fractal2DRunner, if not duplicate request
		if(fractal2DRunner.isDuplicate(toGenerate, relativePath_full)) {
			System.out.println("generateFractal2D: duplicate " + toGenerate.getClass().getSimpleName() + " request.");
			model.addAllAttributes(GenerateFractalController.model.asMap());
			model.addAttribute("imagePath", fractalImagePath);
			return false;
		} else {
			setFractal2DModelParams(title, action, fractalImagePath, params_page, toGenerate.width, 
								    toGenerate.height, toGenerate.totalIterations, model);

			fractal2DRunner.generateAndOutputToFile(modelParamSetter, toGenerate, relativePath, filename, true, false);
			return true;
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
		model.addAttribute("parseFailedMessage", "");
		GenerateFractalController.model = model;
	}
	
	private void setFractal2DModelParam_loadingMessage(String loadingMessage, Model model)
	{
		// This updates server-side model only. The client must get the updated attribute with 
		// URI /get-loading-message
		model.addAttribute("loadingMessage", loadingMessage);
	}
}
