package entrega1;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ListenerDeContexto implements ServletContextListener {

	private ServletContext context = null;

	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		context.setAttribute("url", "http://clima.info.unlp.edu.ar/last");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}