package br.edu.unoesc.calculadora.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.unoesc.calculadora.model.Calculadora;

@Controller
public class CalculadoraController {
	@GetMapping("/soma-query")
    public String somaQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
    						@RequestParam(value = "n2", defaultValue = "0") String n2,
    						RedirectAttributes atributos) {	
		atributos.addAttribute("v1", "40");
		atributos.addFlashAttribute("v2", "42");
		
    	return "redirect:/soma-path/" + n1 + "/" + n2;
    }
	
    @GetMapping("/soma-path/{numero1}/{numero2}")
    public String somaPath(@PathVariable String numero1, 
    					   @PathVariable String numero2,
    					   @ModelAttribute("v1") Double v1,
    					   @ModelAttribute("v2") Double v2,
    					   ModelMap model) {
    	System.out.println(v1 + " " + v2);
    	System.out.println(model.getAttribute("v1") + " " + model.getAttribute("v2"));
    	
    	model.addAttribute("resultado", Calculadora.somar(numero1, numero2));
    	
    	return "resultado"; 
    }
    //----------------
    @GetMapping("/soma-forward-query")
    public String somaForwardQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
    						       @RequestParam(value = "n2", defaultValue = "0") String n2,
    						       ModelMap model,
    						       HttpServletRequest requisicao) {	
		model.addAttribute("v1", "40");
    	requisicao.setAttribute("v2", "2");
		
    	return "forward:/soma-forward-path/" + n1 + "/" + n2;
    }
    
    @GetMapping("/soma-forward-path/{numero1}/{numero2}")
    public String somaForwardPath(@PathVariable String numero1, 
    							  @PathVariable String numero2,
    							  Model model,
    							  HttpServletRequest requisicao) {
    	System.out.println(requisicao.getAttribute("v1"));
    	System.out.println(requisicao.getAttribute("v2"));

    	model.addAttribute("resultado", Calculadora.somar(numero1, numero2));
    	
    	return "resultado"; 
    }
    
    @GetMapping("/subtrai-query")
    public String subtraiQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
                               @RequestParam(value = "n2", defaultValue = "0") String n2,
                               RedirectAttributes atributos) { 
        atributos.addAttribute("v1", "10.5");
        atributos.addFlashAttribute("v2", "5.5");
        
        return "redirect:/subtrai-path/" + n1 + "/" + n2;
    }

    @GetMapping("/subtrai-path/{numero1}/{numero2}")
    public String subtraiPath(@PathVariable String numero1, 
                              @PathVariable String numero2,
                              @ModelAttribute("v1") Double v1,
                              @ModelAttribute("v2") Double v2,
                              ModelMap model) {
        System.out.println(v1 + " " + v2);
        System.out.println(model.getAttribute("v1") + " " + model.getAttribute("v2"));
        
        model.addAttribute("resultado", Calculadora.subtrair(numero1, numero2));
        
        return "resultado"; 
    }
    
    @GetMapping("/multiplica-query")
    public String multiplicaQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
                                  @RequestParam(value = "n2", defaultValue = "0") String n2,
                                  ModelMap model,
                                  HttpServletRequest request) {
        model.addAttribute("v1", "10");
        request.setAttribute("v2", "5");

        return "forward:/multiplica-path/" + n1 + "/" + n2;
    }

    @GetMapping("/multiplica-path/{numero1}/{numero2}")
    public String multiplicaPath(@PathVariable String numero1,
                                 @PathVariable String numero2,
                                 Model model,
                                 HttpServletRequest request) {
        System.out.println(request.getAttribute("v1"));
        System.out.println(request.getAttribute("v2"));

        Double resultado = Calculadora.multiplicar(numero1, numero2);
        model.addAttribute("resultado", resultado);

        return "resultado";
    }


}