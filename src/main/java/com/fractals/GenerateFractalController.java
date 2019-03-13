package com.fractals;

import java.util.Date;
import java.util.Optional;

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
import com.fractals.DB.FractalFlowerEntity;
import com.fractals.DB.FractalTreeEntity;

/**
 * GenerateFractalController --- 
 * 			Holds the logic to map user-inputted URI to fractal generation via Spring
 * 		    and then display the fractal HTML page.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   03/02/2019
 */
@Controller
public class GenerateFractalController 
{	
	private final String pageTitle_FractalTree = "Fractal Tree";
	private final String pageTitle_FractalCircle = "Fractal Circles";
	private final String pageTitle_FractalFlower = "Fractal Flower";
	
	/**
	 * Queue a FractalTree for async generation, and return fractal.html
	 * with params fragment set to fragments/fractal-tree-params.html
	 * @param _width Width of the image to generate.
	 * @param _height Height of the image to generate.
	 * @param _iterations Number of fractal iterations to perform.
	 * @param _angle Angle in degrees between child nodes in the fractal.
	 * @param _factor Scaling factor for each child node in the fractal.
	 * @param _padding_w Horizontal padding in the image to generate.
	 * @param _padding_h Vertical padding in the image to generate.
	 * @param _model Thymeleaf page model
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
			double factor = Helper.parseNonNegativeDoubleParam("Scaling Factor", _factor, parseFailedMessage);
			int padding_w = Helper.parseNonNegativeIntParam("padding_w", _padding_w, parseFailedMessage);
			int padding_h = Helper.parseNonNegativeIntParam("padding_h", _padding_h, parseFailedMessage);
			
			//Parse success: perform actual generation
			return _generateFractalTree(width, height, iterations, angle, factor, padding_w, 
										padding_h, model);
		} 
		catch (Exception e) 
		{
			//Parsing failed
			System.out.println("generateFractalTree: " + parseFailedMessage.value);
			//TODO create parse failed image
			setFractalTreeModelParams("images/TODOparsefailedimage.png", parseFailedMessage.value, _width, _height, 
									  _iterations, _angle, _factor, model);
			return "fractal";
		}
	}
	
	private String _generateFractalTree(int width, int height, int iterations, double angle, double factor, 
										int padding_w, int padding_h, Model model)
	{
		double angle_rad = Math.toRadians(angle);
		
		FractalTree fractalTree = new FractalTree(width, height, iterations, angle_rad,
												  factor, padding_w, padding_h);
		
		//WARNING: since we create a new Fractal2DEntity for each request and don't recycle IDs, the IDs could skyrocket quickly.
		FractalTreeEntity newEntity = new FractalTreeEntity(fractalTree);
		
		// Generate on separate thread using Fractal2DRunner, if match not found in DB
		FractalTreeEntity dbEntity = Fractal2DRunner_new.generate(newEntity, false);
		FractalTreeEntity toUse = newEntity;
		if(dbEntity == null) //not in DB yet
		{
			toUse = newEntity;
		} else { //already present in DB; in-progress or already generated
			toUse = dbEntity;
			System.out.println("generateFractal2D: existing " + dbEntity.getClass().getSimpleName() + " found in database.");
		}
		setFractalTreeModelParams(toUse.getImageSrc(), toUse.getLoadingMessage(), String.valueOf(width), 
								  String.valueOf(height), String.valueOf(iterations), String.valueOf(angle), String.valueOf(factor), model);
		return "fractal";
	}
	
	//TODO temp for debugging
	@GetMapping("/db-all-trees")
	public @ResponseBody Iterable<FractalTreeEntity> listDB_fractalTrees()
	{
		return DB.getFractalTreeEntities().findAll();
	}
	
	//TODO temp for debugging
	@GetMapping("/db-all-circles")
	public @ResponseBody Iterable<FractalCircleEntity> listDB_fractalCircles()
	{
		return DB.getFractalCircleEntities().findAll();
	}
	
	//TODO temp for debugging
	@GetMapping("/db-all-flowers")
	public @ResponseBody Iterable<FractalFlowerEntity> listDB_fractalFlowers() {
		return DB.getFractalFlowerEntities().findAll();
	}
	
	/**
	 * Queue a FractalCircle for async generation, and return fractal.html
	 * with params fragment set to fragments/fractal-circle-params.html
	 * @param _width Width of the image to generate.
	 * @param _height Height of the image to generate.
	 * @param _iterations Number of fractal iterations to perform.
	 * @param _satellites Number of child node 'satellites' to generate per iteration.
	 * @param _factor Scaling factor for each child node in the fractal.
	 * @param _padding_w Horizontal padding in the image to generate.
	 * @param _padding_h Vertical padding in the image to generate.
	 * @param model Thymeleaf page model
	 * @return fractal.html
	 */
	@GetMapping("/fractal-circle")
	public String generateFractalCircle(
			@RequestParam(name="w", required=false, defaultValue="700") String _width,
			@RequestParam(name="h", required=false, defaultValue="500") String _height,
			@RequestParam(name="i", required=false, defaultValue="4") String _iterations,
			@RequestParam(name="satellites", required=false, defaultValue="3") String _satellites,
			@RequestParam(name="factor", required=false, defaultValue="0.5") String _factor,
			@RequestParam(name="padding_w", required=false, defaultValue="40") String _padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") String _padding_h,
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
			double factor = Helper.parseNonNegativeDoubleParam("Scaling Sactor", _factor, parseFailedMessage);
			int padding_w = Helper.parseNonNegativeIntParam("padding_w", _padding_w, parseFailedMessage);
			int padding_h = Helper.parseNonNegativeIntParam("padding_h", _padding_h, parseFailedMessage);
			
			//Parse success: perform actual generation
			return _generateFractalCircle(width, height, iterations, satellites, factor, padding_w, 
										  padding_h, model);
		} 
		catch (Exception e) 
		{
			// Parsing failed
			System.out.println("generateFractalCircle: " + parseFailedMessage.value);
			//TODO create parse failed image
			setFractalCircleModelParams("TODOparseerrorimage.png", parseFailedMessage.value, _width, _height, 
										_iterations, _satellites, _factor, model);
			return "fractal";
		}
	}
	
	private String _generateFractalCircle(int width, int height, int iterations, int satellites,
			double factor, int padding_w, int padding_h, Model model)
	{
		FractalCircle fractalCircle = new FractalCircle(width, height, iterations, satellites, factor, padding_w, padding_h);

		//WARNING: since we create a new Fractal2DEntity for each request and don't recycle IDs, the IDs could skyrocket quickly.
		FractalCircleEntity newEntity = new FractalCircleEntity(fractalCircle);

		// Generate on separate thread using Fractal2DRunner, if match not found in DB
		FractalCircleEntity dbEntity = Fractal2DRunner_new.generate(newEntity, false);
		FractalCircleEntity toUse = newEntity;
		if (dbEntity == null) // not in DB yet
		{
			toUse = newEntity;
		} else { // already present in DB; in-progress or already generated
			toUse = dbEntity;
			System.out.println("generateFractal2D: existing " + dbEntity.getClass().getSimpleName() + " found in database.");
		}
		setFractalCircleModelParams(toUse.getImageSrc(), toUse.getLoadingMessage(), String.valueOf(width), 
									String.valueOf(height), String.valueOf(iterations), String.valueOf(satellites), String.valueOf(factor), model);
		return "fractal";
	}
	
	/**
	 * Queue a FractalFlower for async generation, and return fractal.html
	 * with params fragment set to fragments/fractal-flower-params.html
	 * @param _width Width of the image to generate.
	 * @param _height Height of the image to generate.
	 * @param _iterations Number of fractal iterations to perform.
	 * @param _petals Number of child node petals to generate per iteration.
	 * @param _arcAngle Angle (degrees) that a petal arc should span (e.g. 180 is semicircle)
	 * @param _factor Scaling factor for each child node in the fractal.
	 * @param _padding_w Horizontal padding in the image to generate.
	 * @param _padding_h Vertical padding in the image to generate.
	 * @param model Thymeleaf page model
	 * @return fractal.html
	 */
	@GetMapping("/fractal-flower")
	public String generateFractalFlower(
			@RequestParam(name="w", required=false, defaultValue="700") String _width,
			@RequestParam(name="h", required=false, defaultValue="500") String _height,
			@RequestParam(name="i", required=false, defaultValue="5") String _iterations,
			@RequestParam(name="petals", required=false, defaultValue="8") String _petals,
			@RequestParam(name="arcAngle", required=false, defaultValue="180") String _arcAngle,
			@RequestParam(name="factor", required=false, defaultValue="0.5") String _factor,
			@RequestParam(name="power", required=false, defaultValue="2.0") String _power,
			@RequestParam(name="padding_w", required=false, defaultValue="40") String _padding_w,
			@RequestParam(name="padding_h", required=false, defaultValue="40") String _padding_h,
			Model model)
	{
		// Attempt to parse params to numerical types
		Helper.Wrapper<String> parseFailedMessage = new Helper.Wrapper<String>("");
		try 
		{
			int width = Helper.parseNonNegativeIntParam("Width", _width, parseFailedMessage);
			int height = Helper.parseNonNegativeIntParam("Height", _height, parseFailedMessage);
			int iterations = Helper.parseNonNegativeIntParam("Iterations", _iterations, parseFailedMessage);
			int petals = Helper.parseNonNegativeIntParam("Petals", _petals, parseFailedMessage);
			double arcAngle = Helper.parseDoubleParam("Arc Angle", _arcAngle, parseFailedMessage);
			double factor = Helper.parseNonNegativeDoubleParam("Scaling Factor", _factor, parseFailedMessage);
			double power = Helper.parseDoubleParam("Scaling Power", _power, parseFailedMessage);
			int padding_w = Helper.parseNonNegativeIntParam("padding_w", _padding_w, parseFailedMessage);
			int padding_h = Helper.parseNonNegativeIntParam("padding_h", _padding_h, parseFailedMessage);
			
			//Parse success: perform actual generation
			return _generateFractalFlower(width, height, iterations, petals, arcAngle, factor, power, padding_w, padding_h, model);
		} 
		catch (Exception e) 
		{
			// Parsing failed
			System.out.println("generateFractalFlower: " + parseFailedMessage.value);
			//TODO create parse failed image
			setFractalFlowerModelParams("TODOparseerrorimage.png", parseFailedMessage.value, _width, _height, 
										_iterations, _petals, _arcAngle, _factor, _power, model);
			return "fractal";
		}
	}
	
	private String _generateFractalFlower(int width, int height, int iterations, int petals, double arcAngle,
										  double factor, double power, int padding_w, int padding_h, Model model)
	{
		double arcAngle_rad = Math.toRadians(arcAngle);
		FractalFlower fractalFlower = new FractalFlower(width, height, iterations, petals, arcAngle_rad, factor, power, padding_w, padding_h);

		//WARNING: since we create a new Fractal2DEntity for each request and don't recycle IDs, the IDs could skyrocket quickly.
		FractalFlowerEntity newEntity = new FractalFlowerEntity(fractalFlower);

		// Generate on separate thread using Fractal2DRunner, if match not found in DB
		FractalFlowerEntity dbEntity = Fractal2DRunner_new.generate(newEntity, false);
		FractalFlowerEntity toUse = newEntity;
		if (dbEntity == null) // not in DB yet
		{
			toUse = newEntity;
		} else { // already present in DB; in-progress or already generated
			toUse = dbEntity;
			System.out.println("generateFractal2D: existing " + dbEntity.getClass().getSimpleName() + " found in database.");
		}
		setFractalFlowerModelParams(toUse.getImageSrc(), toUse.getLoadingMessage(), String.valueOf(width), String.valueOf(height), 
				String.valueOf(iterations), String.valueOf(petals), String.valueOf(arcAngle), String.valueOf(factor), String.valueOf(power), model);
		return "fractal";
	}

	/**
	 * Gets a Fractal2DEntity from the DB by ID, and returns its fractal2D.loadingMessage.
	 * @param _id ID of the Fractal2DEntity to find in the DB.
	 * @param pageTitle fractal.html page title (set depending on the type of fractal shown).
	 * @return ResponseEntity containing the loadingMessage.
	 */
	@GetMapping("/get-loading-message")
	public @ResponseBody ResponseEntity<?> getLoadingMessage(
			@RequestParam(name="id", required=true) String _id,
			@RequestParam(name="pageTitle", required=true) String pageTitle)
	{
		Helper.Wrapper<String> parseFailedMessage = new Helper.Wrapper<>("");
		try {
			long id = Helper.parseLongParam("id", _id, parseFailedMessage);
			return _getLoadingMessage(id, pageTitle);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(parseFailedMessage.value);
		}
	}
	
	private ResponseEntity<?> _getLoadingMessage(long id, String pageTitle)
	{
		switch (pageTitle) {
			case pageTitle_FractalTree:
				Optional<FractalTreeEntity> dbEntity = DB.getFractalTreeEntities().findById(id);
				if(dbEntity.isPresent()) {
					return ResponseEntity.ok(dbEntity.get().getLoadingMessage());
				}
				break;
			case pageTitle_FractalCircle:
				Optional<FractalCircleEntity> dbEntity2 = DB.getFractalCircleEntities().findById(id);
				if(dbEntity2.isPresent()) {
					return ResponseEntity.ok(dbEntity2.get().getLoadingMessage());
				}
				break;
			case pageTitle_FractalFlower:
				Optional<FractalFlowerEntity> dbEntity3 = DB.getFractalFlowerEntities().findById(id);
				if(dbEntity3.isPresent()) {
					return ResponseEntity.ok(dbEntity3.get().getLoadingMessage());
				}
				break;
			default:
				return ResponseEntity.badRequest().body("Invalid param pageTitle set; use fractal.html's title as the parameter.");
		}
		return ResponseEntity.badRequest().body("Not found: no Fractal2DEntity with ID#" + id + " found in the database.");
	}
	
	private void setFractalTreeModelParams(String imageSrc, String loadingMessage, String width, 
										   String height, String iterations, String angle, String factor, Model model)
	{
		setFractal2DModelParams("Fractal Tree", "/fractal-tree", imageSrc,
				"fragments/fractal-tree-params.html", loadingMessage, 
				width, height, iterations, model);
		// Set FractalTree-specific attributes
		model.addAttribute("angle", angle);
		model.addAttribute("factor", factor);
	}
	
	private void setFractalCircleModelParams(String imageSrc, String loadingMessage, String width, 
											 String height, String iterations, String satellites, String factor, Model model)
	{
		setFractal2DModelParams("Fractal Circles", "/fractal-circle", imageSrc,
							    "fragments/fractal-circle-params.html", loadingMessage, 
							    width, height, iterations, model);
		// Set FractalCircle-specific attributes
		model.addAttribute("satellites", satellites);
		model.addAttribute("factor", factor);
	}
	
	private void setFractalFlowerModelParams(String imageSrc, String loadingMessage, String width, 
			 String height, String iterations, String petals, String arcAngle, String factor, String power, Model model)
	{
		setFractal2DModelParams("Fractal Flower", "/fractal-flower", imageSrc, 
								"fragments/fractal-flower-params.html", loadingMessage, 
								width, height, iterations, model);
		// Set FractalFlower-specific attributes
		model.addAttribute("petals", petals);
		model.addAttribute("arcAngle", arcAngle);
		model.addAttribute("factor", factor);
		model.addAttribute("power", power);
	}
	
	private void setFractal2DModelParams(String title, String action, String imagePath, String params_page, 
			   							 String loadingMessage, String width, String height, String iterations, Model model)
	{
		model.addAttribute("title", title);
		model.addAttribute("action", action);
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("params_page", params_page);
		model.addAttribute("params_fragment", "params");
		model.addAttribute("width", width);
		model.addAttribute("height", height);
		model.addAttribute("iterations", iterations);
		model.addAttribute("loadingMessage", loadingMessage);
		model.addAttribute("parseFailedMessage", "");
	}
	
}
