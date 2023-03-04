package br.edu.unoesc.calculadora.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import br.edu.unoesc.calculadora.exceptions.RaizNegativaException;
import br.edu.unoesc.calculadora.model.Calculadora;
import br.edu.unoesc.calculadora.utils.ConversorNumerico;

@RestController
public class CalculadoraRestController {
	@GetMapping("/somar-query")
	public RedirectView somarQuery(@RequestParam("numero1") String numero1,
	                               @RequestParam("numero2") String numero2,
	                               RedirectAttributes redirectAttributes) {
	    Double resultado = Calculadora.somar(numero1, numero2);
	    redirectAttributes.addAttribute("resultado", resultado);
	    return new RedirectView("/somar-path");
	}

	@GetMapping("/somar-path/{numero1}/{numero2}")
	public Double somarPath(@PathVariable String numero1, @PathVariable String numero2) {
	    Double num1 = Double.parseDouble(numero1);
	    Double num2 = Double.parseDouble(numero2);
	    return Calculadora.somar(num1, num2);
	}



    //---------------------
    // Redirect: Lado cliente
    @GetMapping("/subtrair-query")
    public ResponseEntity<Void> subtrairQuery(@RequestParam(value = "n1", defaultValue = "0") Double n1,
    										  @RequestParam(value = "n2", defaultValue = "0") Double n2) {
    	return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/subtrair-path/" + n1 + "/" + n2)).build();
    }

	@GetMapping("/subtrair-path/{numero1}/{numero2}")
    public Double subtrairPath(@PathVariable("numero1") String numero1, 
    					       @PathVariable("numero2") String numero2) {
    	return Calculadora.subtrair(numero1, numero2);
    }
	//---------------------
    // Redirect: Lado cliente
    @GetMapping("/multiplicar-query")
    public RedirectView multiplicarQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
    						       		 @RequestParam(value = "n2", defaultValue = "0") String n2,
    						       		 RedirectAttributes atributos) {
    	atributos.addAttribute("v1", 40);
    	atributos.addFlashAttribute("v2", 2);
    	
    	return new RedirectView("/multiplicar-path/" + n1 + "/" + n2);
    }
    
    @GetMapping("/multiplicar-path/{numero1}/{numero2}")
    public Double multiplicarPath(@PathVariable String numero1, 
    							  @PathVariable String numero2,
    							  @ModelAttribute("v1") Double v1,
    							  @ModelAttribute("v2") Double v2,
    							  Model model) {
    	System.out.println(model.getAttribute("v1"));
    	System.out.println(model.getAttribute("v2"));
    	System.out.println(v1 + " " + v2);
    	
    	return Calculadora.multiplicar(numero1, numero2);
    }
    //---------------------
    // Redirect: Lado cliente
    @GetMapping("/dividir-query")
    public ModelAndView dividirQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
    						   		 @RequestParam(value = "n2", defaultValue = "0") String n2,
    						   		 ModelMap modeloMap) {
    	modeloMap.addAttribute("valor", 42);

    	return new ModelAndView("redirect:/dividir-path/" + n1 + "/" + n2, modeloMap);    		
    }
    
    @GetMapping("/dividir-path/{n1}/{n2}")
    public String dividirPath(@PathVariable String n1, 
    						  @PathVariable String n2, 
    						  @ModelAttribute("valor") String valor,
    						  Model modelo) {
    	System.out.println(valor);
    	System.out.println(modelo.getAttribute("valor"));

    	try {
    		return Calculadora.dividir(n1, n2).toString();
    	} catch (ArithmeticException e) {
    		return e.getMessage();
    	}
    }
    //---------------------
    // Forward: Lado servidor (transparente para o cliente/navegador)
    @GetMapping("/calcular-media-query")
    public ModelAndView calcularMediaQuery(@RequestParam(value = "n1", defaultValue = "0") String n1,
    						 	     	   @RequestParam(value = "n2", defaultValue = "0") String n2,
    						 	     	   HttpServletRequest requisicao) {
    	requisicao.setAttribute("valor", "42");
    	
    	return new ModelAndView("forward:/calcular-media-path/" + n1 + "/" + n2);
    }
    
    @GetMapping("/calcular-media-path/{numero1}/{numero2}")
    public Double calcularMediaPath(@PathVariable String numero1,
    					    		@PathVariable String numero2,
    					    		HttpServletRequest requisicao) {
    	System.out.println(requisicao.getAttribute("valor"));
    	
    	return Calculadora.calcularMedia(numero1, numero2);
    }
    //---------------------
    @GetMapping("/calcular-potencia-query")
    public ModelAndView calcularPotenciaQuery(@RequestParam String base, @RequestParam String expoente, RedirectAttributes redirectAttributes) {
        double resultado = Calculadora.calcularPotencia(Double.parseDouble(base), Double.parseDouble(expoente));
        redirectAttributes.addFlashAttribute("resultado", resultado);
        redirectAttributes.addFlashAttribute("mensagem", "Potência calculada com sucesso!");
        return new ModelAndView("redirect:/calcular-potencia-path");
    }
    
    @GetMapping("/calcular-potencia-path/{base}/{expoente}")
    public String calcularPotenciaPath(@ModelAttribute("resultado") Double resultado, @ModelAttribute("mensagem") String mensagem, Model model) {
        System.out.println(mensagem);
        System.out.println("Resultado: " + resultado);
        return "calcular-potencia";
    }
    //---------------------
    @GetMapping("/calcular-raiz-query")
    public ModelAndView calcularRaizQuery(@RequestParam(value = "numero", required = false) String numeroStr,
                                           HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("forward:/calcular-raiz-path");
        if (ConversorNumerico.ehNumerico(numeroStr)) {
            Double numero = ConversorNumerico.converterParaDouble(numeroStr);
            request.setAttribute("numeroCalculado", numero);
            request.setAttribute("mensagem", "Raiz quadrada calculada com sucesso!");
        } else {
            request.setAttribute("mensagem", "O valor informado não é um número válido.");
        }
        return mav;
    }

    
    @GetMapping("/calcular-raiz-path/{numero}")
    public Double calcularRaizPath(@ModelAttribute("numeroCalculado") Double numero,
            @ModelAttribute("mensagem") String mensagem) {
    		System.out.println("Número calculado: " + numero);
    		System.out.println("Mensagem: " + mensagem);
    		return Calculadora.calcularRaizQuadrada(numero);
    }
}
