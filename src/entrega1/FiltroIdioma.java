package entrega1;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/ImagenClima")
public class FiltroIdioma implements Filter {

    public FiltroIdioma() {
        // TODO Auto-generated constructor stub
    }
    
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Se castea para poder usar sus metodos
		HttpServletRequest req = (HttpServletRequest) request;
		String idioma = req.getHeader("Accept-Language");
		request.setAttribute("idioma", idioma);
		chain.doFilter(request, response);
	}

}
